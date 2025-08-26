package com.ds.jlptnoteapp.model.dto;

import lombok.Data;

@Data
public class ExampleDto {
    private Long id;
    private String sampleKanji;
    private String sampleNonKanji;
    private String meaning;
    private String note;
}