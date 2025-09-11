package com.ds.jlptnoteapp.model.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class KatsuyouDto {
    private LemmaDto lemmaDto;
    private ConjugationResultDto result;
    private Map<String,String> formMap;
}