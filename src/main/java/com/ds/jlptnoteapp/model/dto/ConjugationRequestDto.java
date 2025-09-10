package com.ds.jlptnoteapp.model.dto;

import com.ds.jlptnoteapp.model.enums.PosType;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConjugationRequestDto {
    private String headwordKana;    // wajib
    private String kanji;           // opsional
    private PosType posType;        // wajib
    private String godanEndingKana; // wajib jika posType=GODAN (う/つ/る/む/ぶ/ぬ/く/ぐ/す)
}