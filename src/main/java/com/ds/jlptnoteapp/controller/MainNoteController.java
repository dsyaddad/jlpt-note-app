package com.ds.jlptnoteapp.controller;

import com.ds.jlptnoteapp.model.dto.MainNoteDto;
import com.ds.jlptnoteapp.model.entity.MainNote;
import com.ds.jlptnoteapp.model.repository.MainNoteRepository;
import com.ds.jlptnoteapp.model.specification.MainNoteSpecification;
import com.ds.jlptnoteapp.model.transformer.AppMapper;
import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notes")
public class MainNoteController {

    private final MainNoteRepository mainNoteRepository;
    private final AppMapper mainNoteMapper;
    private final GlobalCachedVariable globalCachedVariable;

    @GetMapping
    public String listMainNotes(
            @RequestParam(value = "pattern", required = false) String pattern,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
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

        return "notes/list"; // <-- file HTML konten
    }

    // Show create form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("levels", globalCachedVariable.getLevelMapByLevel());
        model.addAttribute("mainNote", new MainNoteDto());
        model.addAttribute("formAction", "/notes/create");
        model.addAttribute("submitLabel", "Save");
        model.addAttribute("activeTab", "create-edit");
        model.addAttribute("title", "Add New MainNote");
        return "notes/create-edit";
    }

    @PostMapping("/create")
    public String createMainNote(@ModelAttribute("mainNote") MainNoteDto mainNoteDto) {
        MainNote entity = mainNoteMapper.toEntity(mainNoteDto,globalCachedVariable);
        mainNoteRepository.save(entity);
        return "redirect:/notes";
    }
    // Show edit form using create-edit.html
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MainNote mainNote = mainNoteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid note id: " + id));
        MainNoteDto dto = mainNoteMapper.toDto(mainNote);
        model.addAttribute("levels", globalCachedVariable.getLevelMapByLevel());
        model.addAttribute("mainNote", dto);
        model.addAttribute("formAction", "/notes/edit/" + id);
        model.addAttribute("submitLabel", "Update");
        model.addAttribute("activeTab", "create-edit");
        model.addAttribute("title", "Edit MainNote");
        return "notes/create-edit";
    }

    // Process edit form
    @PostMapping("/edit/{id}")
    public String editMainNote(@PathVariable Long id, @ModelAttribute("mainNote") MainNoteDto mainNoteDto) {
        MainNote entity = mainNoteMapper.toEntity(mainNoteDto, globalCachedVariable);
        entity.setId(id);
        mainNoteRepository.save(entity);
        return "redirect:/notes";
    }
    @GetMapping("/view/{id}")
    public String viewMainNote(@PathVariable Long id, Model model) {
        MainNote mainNote = mainNoteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid note id: " + id));
        MainNoteDto dto = mainNoteMapper.toDto(mainNote);
        model.addAttribute("levels", globalCachedVariable.getLevelMapByLevel());
        model.addAttribute("mainNote", dto);
        model.addAttribute("activeTab", "view");
        model.addAttribute("title", "View MainNote");
        return "notes/view";
    }
}