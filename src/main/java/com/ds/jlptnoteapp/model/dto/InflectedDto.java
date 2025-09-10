package com.ds.jlptnoteapp.model.dto;

import com.ds.jlptnoteapp.model.enums.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InflectedDto {
    private FormType formType;
    private String surface;
    private Politeness politeness; // nullable jika tak relevan
    private Polarity polarity;
    private Voice voice;
    private Tense tense;
}