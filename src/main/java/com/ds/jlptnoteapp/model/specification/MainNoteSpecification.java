package com.ds.jlptnoteapp.model.specification;


import com.ds.jlptnoteapp.model.dto.MainNoteDto;
import com.ds.jlptnoteapp.model.entity.MainNote;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class MainNoteSpecification {

    public static Specification<MainNote> filterByDto(MainNoteDto dto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getId() != null) {
                predicates.add(cb.equal(root.get("id"), dto.getId()));
            }
            if (dto.getSection() != null && !dto.getSection().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("section")), "%" + dto.getSection().toLowerCase() + "%"));
            }
            if (dto.getLevelId() != null) {
                predicates.add(cb.equal(root.get("level").get("id"), dto.getLevelId()));
            }
            if (dto.getIdentifier() != null && !dto.getIdentifier().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("identifier")), "%" + dto.getIdentifier().toLowerCase() + "%"));
            }
            if (dto.getPatternName() != null && !dto.getPatternName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("patternName")), "%" + dto.getPatternName().toLowerCase() + "%"));
            }
            if (dto.getMainFunction() != null && !dto.getMainFunction().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("mainFunction")), "%" + dto.getMainFunction().toLowerCase() + "%"));
            }
            if (dto.getMainUseWhen() != null && !dto.getMainUseWhen().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("mainUseWhen")), "%" + dto.getMainUseWhen().toLowerCase() + "%"));
            }
            if (dto.getMainNote() != null && !dto.getMainNote().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("mainNote")), "%" + dto.getMainNote().toLowerCase() + "%"));
            }

            if (dto.getCreatedAt() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), dto.getCreatedAt()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
