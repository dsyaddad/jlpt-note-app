package com.ds.jlptnoteapp.model.entity;

import com.ds.jlptnoteapp.model.enums.PosType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lemma",
        indexes = {
                @Index(name="ix_lemma_headword", columnList="headword_kana"),
                @Index(name="ix_lemma_pos", columnList="pos_type")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lemma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="headword_kana", nullable=false, length=64)
    private String headwordKana;

    @Column(name="kanji", length=64)
    private String kanji;

    @Enumerated(EnumType.STRING)
    @Column(name="pos_type", nullable=false, length=32)
    private PosType posType;

    @Column(name="godan_ending_kana", length=1)
    private String godanEndingKana; // う/つ/る/... (null jika bukan 五段)

    @Column(name="meaning", length=255)
    private String meaning;
}
