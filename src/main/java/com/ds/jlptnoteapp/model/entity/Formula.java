package com.ds.jlptnoteapp.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "formula")
@Setter
@Getter
public class Formula implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_form")
    private String typeForm;

    @Column(name = "pattern")
    private String pattern;

    @Column(name = "is_changes_form")
    private Boolean isChangesForm;

    @Column(name = "before_pattern")
    private String beforePattern;

    @Column(name = "after_pattern")
    private String afterPattern;

    @Column(name = "sub_function")
    private String subFunction;

    @Column(name = "sub_use_when")
    private String subUseWhen;

    @Column(name = "sub_note")
    private String subNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_note_id", nullable = false)
    private MainNote mainNote;

    @OneToMany(mappedBy = "formula", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE) // ⬅️ cegah setExamples(List)
    private List<Example> examples = new ArrayList<>();

    public void addExample(Example e) { if (e != null) { e.setFormula(this); examples.add(e); } }
    public void removeExample(Example e) { if (e != null) { e.setFormula(null); examples.remove(e); } }

}
