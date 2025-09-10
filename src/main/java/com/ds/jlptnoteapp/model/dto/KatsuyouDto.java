package com.ds.jlptnoteapp.model.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class KatsuyouDto {
    private ConjugationRequestDto request;
    private ConjugationResultDto result;
    private Map<String,String> formMap;
}