package com.ds.jlptnoteapp.model.entity;

import jakarta.persistence.*;
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

    @Column(name = "sub_section")
    private String subSection;

    @Column(name = "pattern")
    private String pattern;

    @Column(name = "sub_function")
    private String subFunction;

    @Column(name = "sub_use_when")
    private String subUseWhen;

    @Column(name = "sub_note")
    private String subNote;

    @ManyToOne
    @JoinColumn(name = "main_note_id") // biar bisa diisi otomatis
    private MainNote mainNote;

    @OneToMany(mappedBy = "formula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Example> examples = new ArrayList<>();

}
