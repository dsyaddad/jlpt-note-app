package com.ds.jlptnoteapp.model.entity;

import com.ds.jlptnoteapp.model.enums.FormType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conjugation_override",
        uniqueConstraints = @UniqueConstraint(name="uq_override", columnNames = {"lemma_id","form_type"}),
        indexes = {
                @Index(name="ix_override_lemma", columnList="lemma_id"),
                @Index(name="ix_override_form", columnList="form_type")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConjugationOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK -> lemma
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="lemma_id", nullable=false,
            foreignKey = @ForeignKey(name="fk_override_lemma"))
    private Lemma lemma;

    @Enumerated(EnumType.STRING)
    @Column(name="form_type", nullable=false, length=32)
    private FormType formType;

    @Column(name="surface", nullable=false, length=64)
    private String surface;

    @Column(name="note", length=255)
    private String note;
}