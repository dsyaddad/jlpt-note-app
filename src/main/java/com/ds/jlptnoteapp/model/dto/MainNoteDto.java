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
    private LevelDto level;
    private List<FormulaDto> formulas;

    public void checkMainNoteExists(){
        if(this.mainNote != null){
            this.identifier = this.mainNote;
            this.patternName = this.mainNote;
            this.mainFunction = this.mainNote;
            this.mainUseWhen = this.mainNote;
        }
    }
}
