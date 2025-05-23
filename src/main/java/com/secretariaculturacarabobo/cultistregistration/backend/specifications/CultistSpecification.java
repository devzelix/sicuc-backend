package com.secretariaculturacarabobo.cultistregistration.backend.specifications;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaQuery;

import org.springframework.data.jpa.domain.Specification;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.Cultist;

/**
 * Specification class for building dynamic query filters for Cultist entities.
 * Uses JPA Criteria API to create predicates based on provided filter
 * parameters.
 */
public class CultistSpecification {

    /**
     * Creates a Specification with multiple optional filters for querying Cultist
     * entities.
     * Supports filtering by name/id/phone (with flexible multi-part query),
     * gender, location (municipality, parish),
     * art category and discipline, and health conditions.
     *
     * @param query           A search string for name, ID, or phone (supports 1-4
     *                        parts)
     * @param gender          Filter by gender ("M" or "F")
     * @param municipalityId  Filter by municipality ID
     * @param parishId        Filter by parish ID
     * @param artCategoryId   Filter by art category ID
     * @param artDisciplineId Filter by art discipline ID
     * @param hasDisability   Filter by presence or absence of disability
     * @param hasIllness      Filter by presence or absence of illness
     * @return Specification<Cultist> applying the given filters
     */
    public static Specification<Cultist> withFilters(String query, String gender, Integer municipalityId,
            Integer parishId,
            Integer artCategoryId,
            Integer artDisciplineId, Boolean hasDisability, Boolean hasIllness) {

        return (Root<Cultist> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Process the search query for name, ID number or phone
            if (query != null && !query.trim().isEmpty()) {
                String normalizedQuery = query.trim().toLowerCase();
                String[] parts = normalizedQuery.split("\\s+");

                switch (parts.length) {

                    case 1:
                        // Single word: match start of first or last name, or contain in
                        // idNumber/phoneNumber
                        String part = parts[0];

                        List<Predicate> singleWordPredicates = new ArrayList<>();

                        singleWordPredicates.add(cb.like(cb.lower(root.get("firstName")), part + "%"));
                        singleWordPredicates.add(cb.like(cb.lower(root.get("lastName")), part + "%"));

                        singleWordPredicates.add(cb.like(cb.lower(root.get("idNumber")), "%" + part + "%"));

                        singleWordPredicates.add(cb.like(cb.lower(root.get("phoneNumber")), "%" + part + "%"));

                        // Combine all single word conditions with OR
                        predicates.add(cb.or(singleWordPredicates.toArray(new Predicate[0])));
                        break;

                    case 2:
                        // Two words: try matching full first or last name, or firstName + lastName
                        // pattern
                        String firstTwo = parts[0] + " " + parts[1];

                        predicates.add(cb.or(
                                cb.like(cb.lower(root.get("firstName")), firstTwo + "%"),
                                cb.like(cb.lower(root.get("lastName")), firstTwo + "%"),
                                cb.and(
                                        cb.like(cb.lower(root.get("firstName")), parts[0] + "%"),
                                        cb.like(cb.lower(root.get("lastName")), parts[1] + "%"))));
                        break;
                    case 3:
                        // Three words: consider compound first or last names with combinations
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
                        // Four words: split into full first and last names
                        String fullFirstName = parts[0] + " " + parts[1];
                        String fullLastName = parts[2] + " " + parts[3];

                        predicates.add(cb.and(
                                cb.like(cb.lower(root.get("firstName")), fullFirstName + "%"),
                                cb.like(cb.lower(root.get("lastName")), fullLastName + "%")));
                        break;

                }
            }

            // Add filter by gender if provided
            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            // Filter by municipality if provided
            if (municipalityId != null) {
                predicates.add(cb.equal(root.get("municipality").get("id"), municipalityId));
            }

            // Filter by parish if provided
            if (parishId != null) {
                predicates.add(cb.equal(root.get("parish").get("id"), parishId));
            }

            // Filter by art category if provided
            if (artCategoryId != null) {
                predicates.add(cb.equal(root.get("artCategory").get("id"), artCategoryId));
            }

            // Filter by art discipline if provided
            if (artDisciplineId != null) {
                predicates.add(cb.equal(root.get("artDiscipline").get("id"), artDisciplineId));
            }

            // Filter by presence or absence of disability
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

            // Filter by presence or absence of illness
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

            // Combine all predicates with AND operator
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
