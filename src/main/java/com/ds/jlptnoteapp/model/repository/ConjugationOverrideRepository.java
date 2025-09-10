package com.ds.jlptnoteapp.model.repository;

import com.ds.jlptnoteapp.model.entity.ConjugationOverride;
import com.ds.jlptnoteapp.model.enums.FormType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConjugationOverrideRepository extends JpaRepository<ConjugationOverride, Long> {
    List<ConjugationOverride> findByLemma_Id(Long lemmaId);
    Optional<ConjugationOverride> findByLemma_IdAndFormType(Long lemmaId, FormType formType);
}