package com.ds.jlptnoteapp.model.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ConjugationResultDto {
    private LemmaDto lemma;
    private List<InflectedDto> forms;
}