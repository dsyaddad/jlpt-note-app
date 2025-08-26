package com.ds.jlptnoteapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "formula")
@Setter
@Getter
public class Example implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sample_kanji")
    private String sampleKanji;

    @Column(name = "sample_non_kanji")
    private String sampleNonKanji;

    @Column(name = "meaning")
    private String meaning;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "formula_id", insertable = false, updatable = false)
    private Formula formula;

}