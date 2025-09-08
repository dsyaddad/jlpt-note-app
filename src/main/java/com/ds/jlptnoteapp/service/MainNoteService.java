package com.ds.jlptnoteapp.service;

import com.ds.jlptnoteapp.model.dto.ExampleDto;
import com.ds.jlptnoteapp.model.dto.FormulaDto;
import com.ds.jlptnoteapp.model.dto.MainNoteDto;
import com.ds.jlptnoteapp.model.entity.Example;
import com.ds.jlptnoteapp.model.entity.Formula;
import com.ds.jlptnoteapp.model.entity.MainNote;
import com.ds.jlptnoteapp.model.repository.LevelRepository;
import com.ds.jlptnoteapp.model.repository.MainNoteRepository;
import com.ds.jlptnoteapp.model.specification.MainNoteSpecification;
import com.ds.jlptnoteapp.model.transformer.AppMapper;
import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MainNoteService {
    private final MainNoteRepository mainNoteRepository;
    private final LevelRepository levelRepository;
    private final AppMapper mapper;
    private final GlobalCachedVariable cache;

    public void save(MainNote mainNote) {
        mainNoteRepository.save(mainNote);
    }

    public Page<MainNote> findAllByfilter(MainNoteDto filter, Pageable pageable) {
        return mainNoteRepository.findAll(MainNoteSpecification.filterByDto(filter), pageable);
    }

    public MainNote findById(Long id) {
        return mainNoteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid note id: " + id));
    }

    public void deleteById(Long id) {
        mainNoteRepository.deleteById(id);
    }

    public void deleteAllByIds(List<Long> ids) {
        mainNoteRepository.deleteAllById(ids);
    }

    @Transactional
    public MainNote upsert(MainNoteDto dto) {
        MainNote parent = (dto.getId() != null)
                ? mainNoteRepository.findById(dto.getId()).orElseThrow()
                : new MainNote();

        // update field sederhana parent
        mapper.updateEntity(parent, dto, cache);

        // set Level via managed reference (hindari entity transien)
        if (dto.getLevelId() != null) {
            parent.setLevel(levelRepository.getReferenceById(dto.getLevelId()));
        } else {
            parent.setLevel(null);
        }

        // sinkronisasi anak & cucu
        syncFormulas(parent, dto.getFormulas());

        return mainNoteRepository.save(parent);
    }

    private void syncFormulas(MainNote parent, List<FormulaDto> incoming) {
        final List<Formula> current = parent.getFormulas();          // referensi list yg sama
        final Map<Long, Formula> byId = current.stream()
                .filter(f -> f.getId() != null)
                .collect(Collectors.toMap(Formula::getId, f -> f));
        final Set<Long> keep = new HashSet<>();

        if (incoming != null) {
            for (FormulaDto d : incoming) {
                final boolean empty = isEmptyFormula(d);

                if (d.getId() == null) {
                    if (empty) continue; // new tapi kosong → abaikan
                    Formula neo = mapper.toEntity(d);
                    parent.addFormula(neo);
                    syncExamples(neo, d.getExamples()); // handle grandchildren baru
                } else {
                    Formula cur = byId.get(d.getId());
                    if (empty) { // treat kosong sebagai DELETE
                        if (cur != null) parent.removeFormula(cur);
                        continue;
                    }
                    keep.add(d.getId());
                    if (cur != null) {
                        mapper.updateEntity(cur, d);
                        syncExamples(cur, d.getExamples()); // update grandchildren
                    } else {
                        // id ada di DTO tapi belum ada di koleksi ter-load
                        Formula attached = mapper.toEntity(d);
                        attached.setId(d.getId());
                        parent.addFormula(attached);
                        syncExamples(attached, d.getExamples());
                    }
                }
            }
        }

        // orphan child (tidak ada lagi di DTO) → DELETE via orphanRemoval
        List<Formula> toRemove = current.stream()
                .filter(f -> f.getId() != null && !keep.contains(f.getId()))
                .collect(Collectors.toList());
        toRemove.forEach(parent::removeFormula);
    }

    private void syncExamples(Formula child, List<ExampleDto> incoming) {
        final List<Example> current = child.getExamples();
        final Map<Long, Example> byId = current.stream()
                .filter(e -> e.getId() != null)
                .collect(Collectors.toMap(Example::getId, e -> e));
        final Set<Long> keep = new HashSet<>();

        if (incoming != null) {
            for (ExampleDto d : incoming) {
                final boolean empty = isEmptyExample(d);

                if (d.getId() == null) {
                    if (empty) continue; // new kosong → skip
                    Example neo = mapper.toEntity(d);
                    child.addExample(neo);
                } else {
                    Example cur = byId.get(d.getId());
                    if (empty) { // treat kosong sebagai DELETE
                        if (cur != null) child.removeExample(cur);
                        continue;
                    }
                    keep.add(d.getId());
                    if (cur != null) {
                        mapper.updateEntity(cur, d);
                    } else {
                        Example attached = mapper.toEntity(d);
                        attached.setId(d.getId());
                        child.addExample(attached);
                    }
                }
            }
        }

        // orphan grandchild → DELETE
        List<Example> toRemove = current.stream()
                .filter(e -> e.getId() != null && !keep.contains(e.getId()))
                .collect(Collectors.toList());
        toRemove.forEach(child::removeExample);
    }

    // --- helpers empty-row (supaya “baris kosong” dianggap DELETE, bukan UPDATE ke null) ---
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private static boolean isEmptyFormula(FormulaDto d) {
        return (d == null) || (
                isBlank(d.getTypeForm())
                        && isBlank(d.getPattern())
                        && isBlank(d.getSubFunction())
                        && isBlank(d.getSubUseWhen())
                        && isBlank(d.getSubNote())
                        && (d.getExamples() == null || d.getExamples().isEmpty())
        );
    }

    private static boolean isEmptyExample(ExampleDto d) {
        return (d == null) || (
                isBlank(d.getSampleKanji())
                        && isBlank(d.getSampleNonKanji())
                        && isBlank(d.getMeaning())
                        && isBlank(d.getNote())
        );
    }
}