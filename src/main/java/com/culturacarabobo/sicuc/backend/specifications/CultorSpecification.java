package com.culturacarabobo.sicuc.backend.specifications;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaQuery;

import org.springframework.data.jpa.domain.Specification;

import com.culturacarabobo.sicuc.backend.entities.Cultor;

/**
 * Creates dynamic JPA Specifications for the {@link Cultor} entity.
 * <p>
 * This class is used by {@link com.culturacarabobo.sicuc.backend.services.CultorService}
 * to build complex, multi-parameter search queries.
 */
public class CultorSpecification {

    /**
     * Creates a {@link Specification} for {@link Cultor} using optional filter parameters.
     * <p>
     * This method dynamically builds a JPA query by combining multiple predicates with an AND operator.
     *
     * @param query           Optional search string for name, ID, or phone (supports 1-4 parts).
     * @param gender          Optional filter by gender ("M" or "F").
     * @param municipalityId  Optional filter by municipality ID.
     * @param parishId        Optional filter by parish ID.
     * @param artCategoryId   Optional filter by art category ID.
     * @param artDisciplineId Optional filter by art discipline ID.
     * @param hasDisability   Optional filter by presence (true) or absence (false) of disability.
     * @param hasIllness      Optional filter by presence (true) or absence (false) of illness.
     * @return A {@link Specification<Cultor>} that applies the given filters.
     */
    public static Specification<Cultor> withFilters(String query, String gender, Integer municipalityId,
            Integer parishId,
            Integer artCategoryId,
            Integer artDisciplineId, Boolean hasDisability, Boolean hasIllness) {

        // The Specification is implemented as a lambda function
        return (Root<Cultor> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) -> {
            
            // A list to hold all individual filter conditions (predicates)
            List<Predicate> predicates = new ArrayList<>();

            // --- Full-Text Search Query Logic ---
            if (query != null && !query.trim().isEmpty()) {
                String normalizedQuery = query.trim().toLowerCase();
                String[] parts = normalizedQuery.split("\\s+");

                // This logic dynamically changes the search behavior based on the number of words
                switch (parts.length) {

                    case 1:
                        // 1 word: "Jose" -> (firstName LIKE 'jose%' OR lastName LIKE 'jose%' OR idNumber LIKE '%jose%'...)
                        String part = parts[0];
                        List<Predicate> singleWordPredicates = new ArrayList<>();
                        singleWordPredicates.add(cb.like(cb.lower(root.get("firstName")), part + "%"));
                        singleWordPredicates.add(cb.like(cb.lower(root.get("lastName")), part + "%"));
                        singleWordPredicates.add(cb.like(cb.lower(root.get("idNumber")), "%" + part + "%"));
                        singleWordPredicates.add(cb.like(cb.lower(root.get("phoneNumber")), "%" + part + "%"));
                        
                        predicates.add(cb.or(singleWordPredicates.toArray(new Predicate[0])));
                        break;

                    case 2:
                        // 2 words: "Jose Perez" -> ( (firstName LIKE 'jose perez%') OR (lastName LIKE 'jose perez%') OR
                        //                           (firstName LIKE 'jose%' AND lastName LIKE 'perez%') )
                        String firstTwo = parts[0] + " " + parts[1];
                        predicates.add(cb.or(
                                cb.like(cb.lower(root.get("firstName")), firstTwo + "%"),
                                cb.like(cb.lower(root.get("lastName")), firstTwo + "%"),
                                cb.and(
                                        cb.like(cb.lower(root.get("firstName")), parts[0] + "%"),
                                        cb.like(cb.lower(root.get("lastName")), parts[1] + "%"))));
                        break;
                    case 3:
                        // 3 words: "Jose Angel Perez" -> ( (firstName LIKE 'jose angel%' AND lastName LIKE 'perez%') OR
                        //                                (firstName LIKE 'jose%' AND lastName LIKE 'angel perez%') )
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
                        // 4 words: "Jose Angel Perez Garcia" -> (firstName LIKE 'jose angel%' AND lastName LIKE 'perez garcia%')
                        String fullFirstName = parts[0] + " " + parts[1];
                        String fullLastName = parts[2] + " " + parts[3];

                        predicates.add(cb.and(
                                cb.like(cb.lower(root.get("firstName")), fullFirstName + "%"),
                                cb.like(cb.lower(root.get("lastName")), fullLastName + "%")));
                        break;
                }
            }

            // --- Simple Equality Filters ---
            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            // --- Relational (Join) Filters ---
            if (municipalityId != null) {
                predicates.add(cb.equal(root.join("municipality").get("id"), municipalityId));
            }
            if (parishId != null) {
                predicates.add(cb.equal(root.join("parish").get("id"), parishId));
            }
            if (artCategoryId != null) {
                predicates.add(cb.equal(root.join("artCategory").get("id"), artCategoryId));
            }
            if (artDisciplineId != null) {
                predicates.add(cb.equal(root.join("artDiscipline").get("id"), artDisciplineId));
            }

            // --- Boolean Logic Filters (Has/HasNot) ---
            if (hasDisability != null) {
                if (hasDisability) {
                    // Find cultores WITH a disability (field is not null AND not an empty string)
                    predicates.add(cb.and(
                            cb.isNotNull(root.get("disability")),
                            cb.notEqual(root.get("disability"), "")));
                } else {
                    // Find cultores WITHOUT a disability (field is null OR is an empty string)
                    predicates.add(cb.or(
                            cb.isNull(root.get("disability")),
                            cb.equal(root.get("disability"), "")));
                }
            }

            if (hasIllness != null) {
                if (hasIllness) {
                    // Find cultores WITH an illness
                    predicates.add(cb.and(
                            cb.isNotNull(root.get("illness")),
                            cb.notEqual(root.get("illness"), "")));
                } else {
                    // Find cultores WITHOUT an illness
                    predicates.add(cb.or(
                            cb.isNull(root.get("illness")),
                            cb.equal(root.get("illness"), "")));
                }
            }

            // Combine all individual predicates into a single WHERE clause
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}