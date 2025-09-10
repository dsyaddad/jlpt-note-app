package com.ds.jlptnoteapp.model.dto;

import com.ds.jlptnoteapp.model.enums.PosType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LemmaDto {
    private Long id;
    private String headwordKana;
    private String kanji;
    private PosType posType;
    private String godanEndingKana;
    private String meaning;
}