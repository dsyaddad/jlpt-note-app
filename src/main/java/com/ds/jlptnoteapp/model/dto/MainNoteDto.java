package com.ds.jlptnoteapp.model.dto;

import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import com.ds.jlptnoteapp.util.GlobalUtil;
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

    public void checkFilterAndAdjust(GlobalCachedVariable cachedVariable){
        if(this.mainNote != null){
            this.identifier = this.mainNote;
            this.patternName = this.mainNote;
            this.mainFunction = this.mainNote;
            this.mainUseWhen = this.mainNote;
        }
        if(GlobalUtil.isN1L2(this.section)){
            String[] split = GlobalUtil.splitN1L2(this.section).get();
            this.section = split[1];
            if(levelId == null){
                this.levelId = cachedVariable.getLevelMapByStringLevel().get(split[0]).getId();
            }
        }
    }
}
