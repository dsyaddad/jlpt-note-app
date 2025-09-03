package com.ds.jlptnoteapp.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class JlptWordDto {
    private Long id;
    private String kanji;
    private String kana;
    private String romaji;
    private String meaningEn;
    private String meaningId;
    private String pos;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long levelId;   // FK reference ke Level
    private LevelDto level; // optional join
    private List<JlptExampleDto> examples;
}
