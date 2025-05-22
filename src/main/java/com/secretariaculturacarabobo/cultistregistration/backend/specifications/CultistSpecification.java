package com.secretariaculturacarabobo.cultistregistration.backend.specifications;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaQuery;

import org.springframework.data.jpa.domain.Specification;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.Cultist;

public class CultistSpecification {

    public static Specification<Cultist> withFilters(String query, String gender, Integer municipalityId,
            Integer parishId,
            Integer artCategoryId,
            Integer artDisciplineId, Boolean hasDisability, Boolean hasIllness) {

        return (Root<Cultist> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query != null && !query.trim().isEmpty()) {
                String normalizedQuery = query.trim().toLowerCase();
                String[] parts = normalizedQuery.split("\\s+");

                switch (parts.length) {

                    case 1:
                        String part = parts[0];

                        List<Predicate> singleWordPredicates = new ArrayList<>();

                        singleWordPredicates.add(cb.like(cb.lower(root.get("firstName")), part + "%"));
                        singleWordPredicates.add(cb.like(cb.lower(root.get("lastName")), part + "%"));

                        singleWordPredicates.add(cb.like(cb.lower(root.get("idNumber")), "%" + part + "%"));

                        singleWordPredicates.add(cb.like(cb.lower(root.get("phoneNumber")), "%" + part + "%"));

                        predicates.add(cb.or(singleWordPredicates.toArray(new Predicate[0])));
                        break;

                    case 2:
                        String firstTwo = parts[0] + " " + parts[1];

                        predicates.add(cb.or(
                                cb.like(cb.lower(root.get("firstName")), firstTwo + "%"),
                                cb.like(cb.lower(root.get("lastName")), firstTwo + "%"),
                                cb.and(
                                        cb.like(cb.lower(root.get("firstName")), parts[0] + "%"),
                                        cb.like(cb.lower(root.get("lastName")), parts[1] + "%"))));
                        break;
                    case 3:
                        String firstTwoAsFirstName = parts[0] + " " + parts[1];
                        String lastTwoAsLastName = parts[1] + " " + parts[2];

                        predicates.add(cb.or(
                                cb.and(
                                        cb.like(cb.lower(root.get("firstName")), firstTwoAsFirstName + "%"),
                                        cb.like(cb.lower(root.get("lastName")), parts[2] + "%")),
                                cb.and(
                                        cb.like(cb.lower(root.get("firstName")), parts[0] + "%"),
                                        cb.like(cb.lower(root.get("lastName")), lastTwoAsLastName + "%"))));
                        break;
                    case 4:
                        String fullFirstName = parts[0] + " " + parts[1]; // jose angel
                        String fullLastName = parts[2] + " " + parts[3]; // solett bustamante

                        predicates.add(cb.and(
                                cb.like(cb.lower(root.get("firstName")), fullFirstName + "%"),
                                cb.like(cb.lower(root.get("lastName")), fullLastName + "%")));
                        break;

                }
            }

            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            if (municipalityId != null) {
                predicates.add(cb.equal(root.get("municipality").get("id"), municipalityId));
            }

            if (parishId != null) {
                predicates.add(cb.equal(root.get("parish").get("id"), parishId));
            }

            if (artCategoryId != null) {
                predicates.add(cb.equal(root.get("artCategory").get("id"), artCategoryId));
            }

            if (artDisciplineId != null) {
                predicates.add(cb.equal(root.get("artDiscipline").get("id"), artDisciplineId));
            }

            if (hasDisability != null) {
                if (hasDisability) {
                    predicates.add(cb.and(
                            cb.isNotNull(root.get("disability")),
                            cb.notEqual(root.get("disability"), "")));
                } else {
                    predicates.add(cb.or(
                            cb.isNull(root.get("disability")),
                            cb.equal(root.get("disability"), "")));
                }
            }

            if (hasIllness != null) {
                if (hasIllness) {
                    predicates.add(cb.and(
                            cb.isNotNull(root.get("illness")),
                            cb.notEqual(root.get("illness"), "")));
                } else {
                    predicates.add(cb.or(
                            cb.isNull(root.get("illness")),
                            cb.equal(root.get("illness"), "")));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
