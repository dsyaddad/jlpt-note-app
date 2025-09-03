package com.ds.jlptnoteapp.model.specification;

import com.ds.jlptnoteapp.model.dto.JlptExampleDto;
import com.ds.jlptnoteapp.model.dto.JlptWordDto;
import com.ds.jlptnoteapp.model.entity.JlptExample;
import com.ds.jlptnoteapp.model.entity.JlptWord;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JlptWordSpecification {

    public static Specification<JlptWord> filterByDto(JlptWordDto dto) {
        return (root, query, cb) -> {
            List<Predicate> andPredicates = new ArrayList<>();
            List<Predicate> orPredicates = new ArrayList<>();

            query.distinct(true); // penting untuk menghindari duplikasi akibat join

            // ===== AND filter (exact match) =====
            if (dto.getId() != null) {
                andPredicates.add(cb.equal(root.get("id"), dto.getId()));
            }

            if (dto.getLevelId() != null) {
                andPredicates.add(cb.equal(root.get("levelId"), dto.getLevelId()));
            }

            if (dto.getCreatedAt() != null) {
                andPredicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), dto.getCreatedAt()));
            }

            if (dto.getUpdatedAt() != null) {
                andPredicates.add(cb.greaterThanOrEqualTo(root.get("updatedAt"), dto.getUpdatedAt()));
            }

            // ===== OR filter (JlptWord fields) =====
            if (dto.getKanji() != null && !dto.getKanji().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("kanji")), "%" + dto.getKanji().toLowerCase() + "%"));
            }

            if (dto.getKana() != null && !dto.getKana().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("kana")), "%" + dto.getKana().toLowerCase() + "%"));
            }

            if (dto.getRomaji() != null && !dto.getRomaji().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("romaji")), "%" + dto.getRomaji().toLowerCase() + "%"));
            }

            if (dto.getMeaningEn() != null && !dto.getMeaningEn().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("meaningEn")), "%" + dto.getMeaningEn().toLowerCase() + "%"));
            }

            if (dto.getMeaningId() != null && !dto.getMeaningId().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("meaningId")), "%" + dto.getMeaningId().toLowerCase() + "%"));
            }

            if (dto.getPos() != null && !dto.getPos().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("pos")), "%" + dto.getPos().toLowerCase() + "%"));
            }

            if (dto.getNote() != null && !dto.getNote().isEmpty()) {
                orPredicates.add(cb.like(cb.lower(root.get("note")), "%" + dto.getNote().toLowerCase() + "%"));
            }

            // ===== OR filter (JlptExample fields via join) =====
            Join<JlptWord, JlptExample> exampleJoin = root.join("examples", JoinType.LEFT);

            if (dto.getExamples() != null && !dto.getExamples().isEmpty()) {
                for (JlptExampleDto exDto : dto.getExamples()) {
                    if (exDto.getJpSentence() != null && !exDto.getJpSentence().isEmpty()) {
                        orPredicates.add(cb.like(cb.lower(exampleJoin.get("jpSentence")), "%" + exDto.getJpSentence().toLowerCase() + "%"));
                    }
                    if (exDto.getTranslation() != null && !exDto.getTranslation().isEmpty()) {
                        orPredicates.add(cb.like(cb.lower(exampleJoin.get("translation")), "%" + exDto.getTranslation().toLowerCase() + "%"));
                    }
                    if (exDto.getNote() != null && !exDto.getNote().isEmpty()) {
                        orPredicates.add(cb.like(cb.lower(exampleJoin.get("note")), "%" + exDto.getNote().toLowerCase() + "%"));
                    }
                }
            }

            // ===== Combine predicates =====
            Predicate finalPredicate = cb.conjunction();

            if (!andPredicates.isEmpty()) {
                finalPredicate = cb.and(andPredicates.toArray(new Predicate[0]));
            }

            if (!orPredicates.isEmpty()) {
                Predicate orCombined = cb.or(orPredicates.toArray(new Predicate[0]));
                finalPredicate = cb.and(finalPredicate, orCombined);
            }

            return finalPredicate;
        };
    }
}
