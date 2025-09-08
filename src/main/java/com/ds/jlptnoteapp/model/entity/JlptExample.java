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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private JlptWord word;

    // (Opsional tapi bagus) equals/hashCode berbasis id
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JlptExample)) return false;
        JlptExample other = (JlptExample) o;
        return id != null && id.equals(other.id);
    }
    @Override public int hashCode() { return 31; }
}
