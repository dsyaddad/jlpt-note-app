package com.ds.jlptnoteapp.controller;

import com.ds.jlptnoteapp.model.dto.MainNoteDto;
import com.ds.jlptnoteapp.model.entity.MainNote;
import com.ds.jlptnoteapp.model.transformer.AppMapper;
import com.ds.jlptnoteapp.service.MainNoteService;
import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import com.ds.jlptnoteapp.util.GlobalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notes")
public class MainNoteController {

    private final MainNoteService mainNoteService;
    private final AppMapper mainNoteMapper;
    private final GlobalCachedVariable globalCachedVariable;

    @GetMapping
    public String listMainNotes(
            MainNoteDto filter, // Spring akan otomatis isi dari query param
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            Model model
    ) {
        filter.checkFilterAndAdjust(globalCachedVariable);
        Pageable pageable = PageRequest.of(page, size, Sort.by("level.id","section").descending());
        Page<MainNote> result = mainNoteService.findAllByfilter(filter, pageable);
        Page<MainNoteDto> dtoPage = result.map(mainNoteMapper::toDto);

        model.addAttribute("mainNotes", dtoPage);
        model.addAttribute("filter", filter);
        model.addAttribute("levels", globalCachedVariable.getLevelMapById());
        model.addAttribute("activeTab", "notes");
        model.addAttribute("title", "Main Notes");

        return "notes/list";
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
    public String createMainNote(@ModelAttribute("mainNote") MainNoteDto mainNoteDto, RedirectAttributes redirectAttributes) {
        mainNoteDto.setId(null);
        mainNoteService.upsert(mainNoteDto);
        redirectAttributes.addFlashAttribute("message", String.format("Note %s update successfully.", mainNoteDto.getSection()));
        return "redirect:/notes";
    }
    // Show edit form using create-edit.html
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MainNote mainNote = mainNoteService.findById(id);
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
    public String editMainNote(@PathVariable Long id, @ModelAttribute("mainNote") MainNoteDto mainNoteDto, RedirectAttributes redirectAttributes) {
        MainNote entity = mainNoteMapper.toEntity(mainNoteDto, globalCachedVariable);
        mainNoteDto.setId(id);
        mainNoteService.upsert(mainNoteDto);
        redirectAttributes.addFlashAttribute("message", String.format("Note %s update successfully.", entity.getSection()));
        return "redirect:/notes";
    }
    @GetMapping("/view/{id}")
    public String viewMainNote(@PathVariable Long id, Model model) {
        MainNote mainNote = mainNoteService.findById(id);
        MainNoteDto dto = mainNoteMapper.toDto(mainNote);
        model.addAttribute("levels", globalCachedVariable.getLevelMapByLevel());
        model.addAttribute("mainNote", dto);
        model.addAttribute("activeTab", "view");
        model.addAttribute("title", "View MainNote");
        return "notes/view";
    }
    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        mainNoteService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Note deleted successfully.");
        return "redirect:/notes";
    }

    @PostMapping("/bulk-delete")
    public String bulkDelete(@RequestParam("ids") java.util.List<Long> ids, RedirectAttributes redirectAttributes) {
        if (ids != null && !ids.isEmpty()) {
            mainNoteService.deleteAllByIds(ids);
            redirectAttributes.addFlashAttribute("message", "Selected notes deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("message", "No notes selected for deletion.");
        }
        return "redirect:/notes";
    }

    @GetMapping("/export-dml")
    public String exportDml(RedirectAttributes redirectAttributes) {
        try {
            GlobalUtil.exportDml();
            redirectAttributes.addFlashAttribute("message", "Exported to notesdb.dml successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Error during export: " + e.getMessage());
        }
        return "redirect:/notes";
    }

}