package com.ds.jlptnoteapp.controller;

import com.ds.jlptnoteapp.model.dto.MainNoteDto;
import com.ds.jlptnoteapp.model.entity.MainNote;
import com.ds.jlptnoteapp.model.repository.MainNoteRepository;
import com.ds.jlptnoteapp.model.specification.MainNoteSpecification;
import com.ds.jlptnoteapp.model.transformer.AppMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main-notes")
public class MainNoteController {

    private final MainNoteRepository mainNoteRepository;
    private final AppMapper mainNoteMapper;

    @GetMapping
    public String listMainNotes(
            @RequestParam(value = "pattern", required = false) String pattern,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        MainNoteDto filter = new MainNoteDto();
        filter.setPatternName(pattern);

        Page<MainNote> result = mainNoteRepository.findAll(
                MainNoteSpecification.filterByDto(filter),
                pageable
        );

        Page<MainNoteDto> dtoPage = result.map(mainNoteMapper::toDto);

        model.addAttribute("mainNotes", dtoPage);
        model.addAttribute("pattern", pattern);

        // tambahan untuk layout frame
        model.addAttribute("activeTab", "notes");
        model.addAttribute("title", "Main Notes");

        return "main_notes/list"; // <-- file HTML konten
    }
}