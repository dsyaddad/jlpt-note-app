package com.ds.jlptnoteapp.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jlpt_words")
@Setter
@Getter
public class JlptWord implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kanji")
    private String kanji;

    @Column(name = "section")
    private String section;

    @Column(name = "kana", nullable = false)
    private String kana;

    @Column(name = "romaji")
    private String romaji;

    @Column(name = "meaning_en", columnDefinition = "TEXT")
    private String meaningEn;

    @Column(name = "meaning_id", columnDefinition = "TEXT")
    private String meaningId;

    @Column(name = "pos")
    private String pos;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (examples != null) {
            for (JlptExample f : examples) {
                f.setWord(this);
            }
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "level_id", insertable = false, updatable = false)
    private Level level;

    @Column(name = "level_id")
    private Long levelId;

    @Setter(AccessLevel.NONE) // ⬅️ hentikan setExamples(List)
    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JlptExample> examples = new ArrayList<>();

    public void addExample(JlptExample e) { e.setWord(this); examples.add(e); }
    public void removeExample(JlptExample e) { e.setWord(null); examples.remove(e); }

}

