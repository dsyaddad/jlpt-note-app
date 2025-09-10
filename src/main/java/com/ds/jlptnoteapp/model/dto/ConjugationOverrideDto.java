package com.ds.jlptnoteapp.model.dto;

import com.ds.jlptnoteapp.model.enums.FormType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConjugationOverrideDto {
    private Long id;
    private Long lemmaId;
    private String lemmaHeadword; // convenience untuk UI
    private FormType formType;
    private String surface;
    private String note;
}
