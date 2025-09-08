package com.ds.jlptnoteapp.service;

import com.ds.jlptnoteapp.model.dto.JlptExampleDto;
import com.ds.jlptnoteapp.model.dto.JlptWordDto;
import com.ds.jlptnoteapp.model.entity.JlptExample;
import com.ds.jlptnoteapp.model.entity.JlptWord;
import com.ds.jlptnoteapp.model.repository.JlptWordRepository;
import com.ds.jlptnoteapp.model.repository.LevelRepository;
import com.ds.jlptnoteapp.model.specification.JlptWordSpecification;
import com.ds.jlptnoteapp.model.transformer.AppMapper;
import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JlptWordService {

    private final JlptWordRepository jlptWordRepository;
    private final AppMapper mapper;  // mapper dari dto <-> entity
    private final GlobalCachedVariable globalCachedVariable;
    private final LevelRepository levelRepository;

    public void save(JlptWordDto jlptWordDto) {
        JlptWord entity = mapper.toEntity(jlptWordDto, globalCachedVariable);
        jlptWordRepository.save(entity);
    }

    public Page<JlptWord> findAllByFilter(JlptWordDto filter, Pageable pageable) {
        return jlptWordRepository.findAll(JlptWordSpecification.filterByDto(filter), pageable);
    }

    public JlptWord findById(Long id) {
        return jlptWordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid word id: " + id));
    }

    public void deleteById(Long id) {
        jlptWordRepository.deleteById(id);
    }

    public void deleteAllByIds(List<Long> ids) {
        jlptWordRepository.deleteAllById(ids);
    }

    @Transactional
    public JlptWord upsert(JlptWordDto dto) {
        // 1) Load parent as managed (untuk edit) atau buat baru
        final JlptWord entity = (dto.getId() != null)
                ? jlptWordRepository.findById(dto.getId()).orElseThrow()
                : new JlptWord();

        // 2) Update field-field sederhana parent (TIDAK menyentuh list anak)
        mapper.updateEntity(entity, dto, globalCachedVariable);

        // 3) Level via managed reference (hindari insertable/updatable=false trap)
        if (dto.getLevelId() != null) {
            entity.setLevel(levelRepository.getReferenceById(dto.getLevelId()));
        } else {
            entity.setLevel(null);
        }

        // 4) Sinkronisasi anak-anak (ADD/UPDATE/REMOVE by id)
        syncExamples(entity, dto.getExamples());

        // 5) Cukup panggil save parent → cascade+orphanRemoval akan bekerja
        return jlptWordRepository.save(entity);
    }

    private void syncExamples(JlptWord parent, List<JlptExampleDto> incoming) {
        // ⛑️ pastikan list parent tidak null dan JANGAN ganti referensi list
        final List<JlptExample> current = parent.getExamples();

        // Map existing by id
        final Map<Long, JlptExample> existingById = current.stream()
                .filter(e -> e.getId() != null)
                .collect(Collectors.toMap(JlptExample::getId, e -> e));

        final Set<Long> keepIds = new HashSet<>();

        if (incoming != null) {
            for (JlptExampleDto d : incoming) {
                final boolean empty = isEmptyRow(d);

                if (d.getId() == null) {
                    if (empty) {
                        // NEW tapi kosong → abaikan (jangan insert row kosong)
                        continue;
                    }
                    // NEW normal
                    JlptExample neo = mapper.toEntity(d);
                    parent.addExample(neo);
                } else {
                    // EXISTING
                    JlptExample cur = existingById.get(d.getId());

                    if (empty) {
                        // EXISTING tapi kosong → anggap DELETE
                        if (cur != null) parent.removeExample(cur);
                        // penting: JANGAN update field ke null
                        // dan JANGAN masukkan id ini ke keepIds
                        continue;
                    }

                    // EXISTING normal → UPDATE
                    keepIds.add(d.getId());
                    if (cur != null) {
                        mapper.updateEntity(cur, d);
                    } else {
                        // id ada di DTO tapi belum di koleksi ter-load (jarang)
                        JlptExample attached = new JlptExample();
                        attached.setId(d.getId());
                        mapper.updateEntity(attached, d);
                        parent.addExample(attached);
                    }
                }
            }
        }

        // Orphan = ada di parent tapi tidak ada di DTO (dan tidak ditandai empty→delete di atas)
        final List<JlptExample> toRemove = current.stream()
                .filter(e -> e.getId() != null)
                .filter(e -> !keepIds.contains(e.getId()))
                .collect(Collectors.toList());

        toRemove.forEach(parent::removeExample);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    private boolean isEmptyRow(JlptExampleDto d) {
        return isBlank(d.getJpSentence()) && isBlank(d.getTranslation()) && isBlank(d.getNote());
    }

}
