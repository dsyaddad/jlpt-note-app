package com.ds.jlptnoteapp.model.entity;

import jakarta.persistence.Column;
import lombok.Data;

import java.io.Serializable;

@Data
public class LevelLanguage implements Serializable {

    @Column(name = "id")
    private Long id;
    @Column(name = "level")
    private String level;
    @Column(name = "note")
    private String note;
}
