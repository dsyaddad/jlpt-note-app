package com.ds.jlptnoteapp.model.dto;


import com.ds.jlptnoteapp.model.entity.Level;
import lombok.Data;

@Data
public class LevelDto {
    private Long id;
    private String level;
    private String note;
}