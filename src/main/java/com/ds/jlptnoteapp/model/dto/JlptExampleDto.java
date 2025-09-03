package com.ds.jlptnoteapp.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JlptExampleDto {
    private Long id;
    private String jpSentence;
    private String translation;
    private String note;
    private Long wordId;
}
