package com.ds.jlptnoteapp.service;

import com.ds.jlptnoteapp.model.dto.MainNoteDto;
import com.ds.jlptnoteapp.model.entity.MainNote;
import com.ds.jlptnoteapp.model.repository.MainNoteRepository;
import com.ds.jlptnoteapp.model.specification.MainNoteSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MainNoteService {
    private final MainNoteRepository mainNoteRepository;

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
}