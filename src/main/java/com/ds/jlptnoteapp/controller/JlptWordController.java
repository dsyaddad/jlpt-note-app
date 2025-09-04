package com.ds.jlptnoteapp.controller;

import com.ds.jlptnoteapp.model.dto.JlptWordDto;
import com.ds.jlptnoteapp.model.dto.MainNoteDto;
import com.ds.jlptnoteapp.model.entity.JlptWord;
import com.ds.jlptnoteapp.model.entity.MainNote;
import com.ds.jlptnoteapp.model.transformer.AppMapper;
import com.ds.jlptnoteapp.service.JlptWordService;
import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jisho")
public class JlptWordController {

    private final JlptWordService jlptWordService;
    private final AppMapper mapper;
    private final GlobalCachedVariable globalCachedVariable;

    // ✅ List page with filter + pagination
    @GetMapping
    public String listWords(
            @ModelAttribute("filter") JlptWordDto filter,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Model model
    ) {
        filter.checkNoteExists();
        Page<JlptWord> page = jlptWordService.findAllByFilter(filter, pageable);

        List<JlptWordDto> wordDtos = page.getContent().stream()
                .map(mapper::toDto)
                .toList();

        model.addAttribute("words", wordDtos);
        model.addAttribute("page", page);
        model.addAttribute("filter", filter);
        model.addAttribute("activeTab", "jisho");


        // untuk dropdown Level
        model.addAttribute("levels", globalCachedVariable.getLevelMapById());
        model.addAttribute("posEnums", PosEnum.getPosEnumsByKey());

        return "jisho/list";
    }

    // ✅ Create form
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("jlptWord", new JlptWordDto());
        model.addAttribute("levels", globalCachedVariable.getLevelMapByLevel());
        model.addAttribute("submitLabel", "Save");
        model.addAttribute("activeTab", "jisho");
        model.addAttribute("formAction", "/jisho/create");
        model.addAttribute("posEnums", PosEnum.getPosEnumsByKey());

        return "jisho/create-edit";
    }

    // ✅ Save (create & update)
    @PostMapping("/create")
    public String save(@ModelAttribute("jlptWord") JlptWordDto wordDto,
                       RedirectAttributes redirectAttributes) {
        jlptWordService.save(wordDto);
        redirectAttributes.addFlashAttribute("message", "Word saved successfully!");
        return "redirect:/jisho";
    }

    // ✅ Edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        JlptWord entity = jlptWordService.findById(id);
        model.addAttribute("jlptWord", mapper.toDto(entity));
        model.addAttribute("levels", globalCachedVariable.getLevelMapByLevel());
        model.addAttribute("submitLabel", "Update");
        model.addAttribute("formAction", "/jisho/edit/" + id);
        model.addAttribute("activeTab", "jisho");
        model.addAttribute("posEnums", PosEnum.getPosEnumsByKey());

        return "jisho/create-edit";
    }
    @PostMapping("/edit/{id}")
    public String editMainNote(@PathVariable Long id, @ModelAttribute("jlptWord") JlptWordDto jlptWord, RedirectAttributes redirectAttributes, Map map) {
        jlptWord.setId(id);
        jlptWordService.save(jlptWord);
        redirectAttributes.addFlashAttribute("message", String.format("Word %s update successfully.", jlptWord.getKanji()));
        return "redirect:/jisho";
    }

    // (Optional) ✅ Bulk delete
    @PostMapping("/bulk-delete")
    public String bulkDelete(@RequestParam("ids") List<Long> ids, RedirectAttributes redirectAttributes) {
        jlptWordService.deleteAllByIds(ids);
        redirectAttributes.addFlashAttribute("message", ids.size() + " words deleted successfully!");
        return "redirect:/jisho";
    }
}
