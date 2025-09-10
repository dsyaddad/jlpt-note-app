package com.ds.jlptnoteapp.model.repository;

import com.ds.jlptnoteapp.model.entity.Lemma;
import com.ds.jlptnoteapp.model.enums.PosType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LemmaRepository extends JpaRepository<Lemma, Long> {
    Optional<Lemma> findByHeadwordKana(String headwordKana);
    List<Lemma> findByPosType(PosType posType);
}
