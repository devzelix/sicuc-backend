package com.culturacarabobo.sicuc.backend.specifications;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.culturacarabobo.sicuc.backend.entities.Cultor;

/**
 * Unit tests for {@link CultorSpecification}.
 * <p>
 * This class verifies that the dynamic query building logic correctly generates
 * the required JPA Criteria API calls (e.g., cb.like, root.join, cb.and) when
 * specific filters are provided.
 */
class CultorSpecificationTest {

    // --- Mocks of JPA Criteria API Components ---
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private CriteriaQuery<?> query;
    @Mock
    private Root<Cultor> root;
    @Mock
    private Path<Object> stringPath; // Used as return type for simple fields (get("firstName"))
    @Mock
    private Join<Object, Object> join; // Used as return type for join()
    @Mock
    private Predicate predicate; // Used as return type for cb.and, cb.like, etc.

    /**
     * Initializes the Mockito environment and sets up the basic behavior for the
     * Criteria API objects before each test method.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Standard mock behavior required by the Specification logic
        when(root.get(anyString())).thenReturn(stringPath);
        when(root.join(anyString())).thenReturn(join);
        when(join.get(anyString())).thenReturn(stringPath);

        // Ensure CriteriaBuilder methods return a mock Predicate instead of null
        when(cb.isNotNull(any())).thenReturn(predicate);
        when(cb.isNull(any())).thenReturn(predicate);
        when(cb.equal(any(), any())).thenReturn(predicate);
        when(cb.notEqual(any(), any())).thenReturn(predicate);
        when(cb.like(any(), anyString())).thenReturn(predicate);
        when(cb.and(any(Predicate[].class))).thenReturn(predicate);
        when(cb.or(any(Predicate[].class))).thenReturn(predicate);
    }

    /**
     * Private helper method to execute the Specification's toPredicate method.
     * This avoids repeating the root, query, and cb parameters in every test.
     */
    @SuppressWarnings("null")
    private Predicate executeSpec(String queryStr, String gender, Integer munId, Integer parishId,
                                  Integer artCatId, Integer artDiscId, Boolean hasDisability, Boolean hasIllness) {
        return CultorSpecification.withFilters(queryStr, gender, munId, parishId, artCatId, artDiscId, hasDisability, hasIllness)
                .toPredicate(root, query, cb);
    }

    // ---------------- Parameterized Tests ---------------- //

    /**
     * Tests the {@code switch (parts.length)} logic for single-word and two-word
     * full-text searches.
     * <p>
     * Scenario: A single word should be searched across names and IDs/phones using OR.
     */
    @ParameterizedTest
    @CsvSource({
        "juan",           // Single word query (Case 1)
        "juan perez"      // Two words query (Case 2)
    })
    void testQuery(String searchQuery) {
        executeSpec(searchQuery, null, null, null, null, null, null, null);
        
        // Verifies that cb.like() was called at least once for the query.
        verify(cb, atLeastOnce()).like(any(), contains(searchQuery.split(" ")[0]));
        
        // Verifies that the predicates were combined using OR (Case 1 or Case 2 logic).
        verify(cb, atLeastOnce()).or(any(Predicate[].class)); 
    }

    /**
     * Tests simple equality filtering by gender.
     */
    @ParameterizedTest
    @CsvSource({
        "M",
        "F"
    })
    void testGenderFilter(String gender) {
        executeSpec(null, gender, null, null, null, null, null, null);
        
        // Verifies the exact comparison call: cb.equal(root.get("gender"), "M")
        verify(cb, atLeastOnce()).equal(stringPath, gender);
    }

    /**
     * Tests filtering by Joins (Foreign Keys) like Municipality/Parish and
     * ArtCategory/ArtDiscipline.
     * <p>
     * Verifies that the appropriate {@code root.join()} call is made.
     */
    @ParameterizedTest
    @CsvSource({
        "1,2",   // municipalityId, parishId
        "3,4"   // artCategoryId, artDisciplineId
    })
    void testJoins(Integer firstId, Integer secondId) {
        // Test Case 1: Municipality/Parish
        executeSpec(null, null, firstId, secondId, null, null, null, null);
        verify(root, atLeastOnce()).join("municipality");
        verify(root, atLeastOnce()).join("parish");
        
        // Test Case 2: ArtCategory/ArtDiscipline
        executeSpec(null, null, null, null, firstId, secondId, null, null);
        verify(root, atLeastOnce()).join("artCategory");
        verify(root, atLeastOnce()).join("artDiscipline");
    }

    /**
     * Tests the complex boolean logic for disability and illness filters.
     * <p>
     * Verifies that the correct combination of {@code isNotNull}, {@code isNull},
     * {@code and}, and {@code or} are called based on the boolean inputs.
     */
    @ParameterizedTest
    @CsvSource({
        "true,false",  // Has Disability (AND check)
        "false,true",   // Has Illness (AND check)
        "false,false",  // Both false (OR checks)
        "true,true"   // Both true (Two AND checks combined with the final Specification AND)
    })
    void testBooleanFilters(Boolean hasDisability, Boolean hasIllness) {
        Predicate result = executeSpec(null, null, null, null, null, null, hasDisability, hasIllness);
        assertNotNull(result);

        if (Boolean.TRUE.equals(hasDisability)) {
            // Check for IS NOT NULL AND NOT EQUAL ("")
            verify(cb, atLeastOnce()).isNotNull(root.get("disability"));
            verify(cb, atLeastOnce()).notEqual(root.get("disability"), "");
        }

        if (Boolean.FALSE.equals(hasIllness)) {
            // Check for IS NULL OR EQUAL ("")
            verify(cb, atLeastOnce()).isNull(root.get("illness"));
            verify(cb, atLeastOnce()).equal(root.get("illness"), "");
        }
    }
}