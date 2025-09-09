package com.ds.jlptnoteapp.model.dto;

import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import com.ds.jlptnoteapp.util.GlobalUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class JlptWordDto {
    private Long id;
    private String section;
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

    public void adjustFilter(GlobalCachedVariable globalCachedVariable){
        if(this.note != null){
            this.kanji = this.note;
            this.kana = this.note;
            this.romaji = this.note;
            this.meaningEn = this.note;
            this.meaningId = this.note;
        }
        if(GlobalUtil.isN1L2(this.section)){
            String[] split = GlobalUtil.splitN1L2(this.section).get();
            this.section = split[1];
            if(levelId == null){
                this.levelId = globalCachedVariable.getLevelMapByStringLevel().get(split[0]).getId();
            }
        }
    }
}
