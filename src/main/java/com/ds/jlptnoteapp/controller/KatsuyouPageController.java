package com.ds.jlptnoteapp.controller;

import com.ds.jlptnoteapp.model.dto.ConjugationRequestDto;
import com.ds.jlptnoteapp.model.dto.ConjugationResultDto;
import com.ds.jlptnoteapp.model.dto.KatsuyouDto;
import com.ds.jlptnoteapp.service.ConjugationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/katsuyou")
@RequiredArgsConstructor
public class KatsuyouPageController {

    private final ConjugationService conjugationService;

    /**
     * Path file JSON bisa dioverride di application.properties:
     * katsuyou.display.json=/prop/conjugation-display.json
     */
    @Value("${katsuyou.display.json}")
    private String displayJsonPath;

    @GetMapping
    public String listSample(Model model) {
        List<ConjugationRequestDto> requests =
                conjugationService.loadConjugationRequestsFromJson(displayJsonPath);

        List<KatsuyouDto> items = requests.stream()
                .map(req -> {
                    ConjugationResultDto result = conjugationService.previewByInput(req);
                    Map<String,String> formMap = result.getForms().stream()
                            .collect(Collectors.toMap(
                                    f -> f.getFormType().name(),   // key = String
                                    f -> f.getSurface(),
                                    (a,b) -> a,
                                    LinkedHashMap::new
                            ));
                    return KatsuyouDto.builder()
                            .request(req)
                            .result(result)
                            .formMap(formMap)
                            .build();
                })
                .toList();

        // kumpulkan semua formTypes unik
        Set<String> formTypes = items.stream()
                .flatMap(i -> i.getFormMap().keySet().stream())
                .collect(Collectors.toCollection(TreeSet::new));

        model.addAttribute("items", items);
        model.addAttribute("formTypes", formTypes);
        model.addAttribute("activeTab", "katsuyou");
        model.addAttribute("title", "Katsuyou");
        return "katsuyou/list";
    }



}