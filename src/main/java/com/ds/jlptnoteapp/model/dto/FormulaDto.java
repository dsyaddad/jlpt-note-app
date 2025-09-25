package com.ds.jlptnoteapp.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class FormulaDto {
    private Long id;
    private Long mainNoteId;
    private String typeForm;
    private String pattern;
    private Boolean isChangesForm;
    private String beforePattern;
    private String afterPattern;
    private String subFunction;
    private String subUseWhen;
    private String subNote;
    private List<ExampleDto> examples;
}