package com.ds.jlptnoteapp.controller;

import com.ds.jlptnoteapp.model.dto.ConjugationRequestDto;
import com.ds.jlptnoteapp.model.dto.ConjugationResultDto;
import com.ds.jlptnoteapp.service.ConjugationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conjugate")
@RequiredArgsConstructor
public class ConjugationController {

    private final ConjugationService service;

    // GET /api/conjugate?lemmaId=1
    @GetMapping
    public ResponseEntity<ConjugationResultDto> byLemmaId(@RequestParam("lemmaId") Long lemmaId) {
        return ResponseEntity.ok(service.generateByLemmaId(lemmaId));
    }

    // POST /api/conjugate/preview  (body: ConjugationRequestDTO)
    @PostMapping("/preview")
    public ResponseEntity<ConjugationResultDto> preview(@RequestBody ConjugationRequestDto req) {
        return ResponseEntity.ok(service.previewByInput(req));
    }
}