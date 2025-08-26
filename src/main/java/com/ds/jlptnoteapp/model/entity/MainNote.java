package com.ds.jlptnoteapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "main_note")
@Setter
@Getter
public class MainNote implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "section")
    private String section;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "pattern_name")
    private String patternName;

    @Column(name = "main_function")
    private String mainFunction;

    @Column(name = "main_use_when")
    private String mainUseWhen;

    @Column(name = "main_note")
    private String mainNote;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @ManyToOne
    @JoinColumn(name = "level_id", insertable = false, updatable = false)
    private Level level;

    @OneToMany(mappedBy = "mainNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Formula> formulas;

}