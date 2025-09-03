package com.ds.jlptnoteapp.service;

import com.ds.jlptnoteapp.model.dto.JlptWordDto;
import com.ds.jlptnoteapp.model.entity.JlptWord;
import com.ds.jlptnoteapp.model.repository.JlptWordRepository;
import com.ds.jlptnoteapp.model.specification.JlptWordSpecification;
import com.ds.jlptnoteapp.model.transformer.AppMapper;
import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JlptWordService {

    private final JlptWordRepository jlptWordRepository;
    private final AppMapper mapper;  // mapper dari dto <-> entity
    private final GlobalCachedVariable globalCachedVariable;

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
}
