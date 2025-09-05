package com.ds.jlptnoteapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "jlpt_examples")
@Setter
@Getter
public class JlptExample implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jp_sentence", columnDefinition = "TEXT")
    private String jpSentence;

    @Column(name = "translation", columnDefinition = "TEXT")
    private String translation;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private JlptWord word;

}
