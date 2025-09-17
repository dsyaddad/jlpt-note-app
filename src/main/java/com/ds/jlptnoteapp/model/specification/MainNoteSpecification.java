package com.ds.jlptnoteapp.model.specification;


import com.ds.jlptnoteapp.model.dto.MainNoteDto;
import com.ds.jlptnoteapp.model.entity.Formula;
import com.ds.jlptnoteapp.model.entity.MainNote;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class MainNoteSpecification {

    public static Specification<MainNote> filterByDto(MainNoteDto dto) {
        return (root, query, cb) -> {
            List<Predicate> andPredicates = new ArrayList<>();
            List<Predicate> orPredicates = new ArrayList<>();

            if (dto.getId() != null) {
                andPredicates.add(cb.equal(root.get("id"), dto.getId()));
            }
            if (dto.getSection() != null && !dto.getSection().isEmpty()) {
                andPredicates.add(cb.like(cb.lower(root.get("section")), "%" + dto.getSection().toLowerCase() + "%"));
            }
            if (dto.getLevelId() != null) {
                andPredicates.add(cb.equal(root.get("level").get("id"), dto.getLevelId()));
            }

            // --- ini dijadikan OR ---
            if (dto.getIdentifier() != null && !dto.getIdentifier().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("identifier")), "%" + dto.getIdentifier().toLowerCase() + "%"));
            }
            if (dto.getPatternName() != null && !dto.getPatternName().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("patternName")), "%" + dto.getPatternName().toLowerCase() + "%"));
            }
            if (dto.getMainFunction() != null && !dto.getMainFunction().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("mainFunction")), "%" + dto.getMainFunction().toLowerCase() + "%"));
            }
            if (dto.getMainUseWhen() != null && !dto.getMainUseWhen().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("mainUseWhen")), "%" + dto.getMainUseWhen().toLowerCase() + "%"));
            }
            if (dto.getMainNote() != null && !dto.getMainNote().isEmpty()) {
                String keyword = "%" + dto.getMainNote().toLowerCase() + "%";

                // search di MainNote.mainNote
                orPredicates.add(cb.like(cb.lower(root.get("mainNote")), keyword));

                // join ke Formula
                Join<MainNote, Formula> formulaJoin = root.join("formulas", JoinType.LEFT);

                // search di field formulas.*
                orPredicates.add(cb.like(cb.lower(formulaJoin.get("pattern")), keyword));
                orPredicates.add(cb.like(cb.lower(formulaJoin.get("subFunction")), keyword));
                orPredicates.add(cb.like(cb.lower(formulaJoin.get("subUseWhen")), keyword));
                orPredicates.add(cb.like(cb.lower(formulaJoin.get("subNote")), keyword));
            }

            if (dto.getCreatedAt() != null) {
                andPredicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), dto.getCreatedAt()));
            }

            // Combine: AND + (OR group)
            Predicate finalPredicate = cb.and(andPredicates.toArray(new Predicate[0]));

            if (!orPredicates.isEmpty()) {
                Predicate orGroup = cb.or(orPredicates.toArray(new Predicate[0]));
                finalPredicate = cb.and(finalPredicate, orGroup);
            }

            return finalPredicate;
        };
    }


}
