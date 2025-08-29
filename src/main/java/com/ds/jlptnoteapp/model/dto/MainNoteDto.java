package com.ds.jlptnoteapp.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MainNoteDto {
    private Long id;
    private String section;
    private String identifier;
    private String patternName;
    private String mainFunction;
    private String mainUseWhen;
    private String mainNote;
    private LocalDateTime createdAt;
    private Long levelId;
    private List<FormulaDto> formulas;
}