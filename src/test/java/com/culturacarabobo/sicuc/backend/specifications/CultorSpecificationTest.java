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

class CultorSpecificationTest {

    @Mock private CriteriaBuilder cb;
    @Mock private CriteriaQuery<?> query;
    @Mock private Root<Cultor> root;
    @Mock private Path<Object> stringPath;
    @Mock private Join<Object, Object> join;
    @Mock private Predicate predicate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(root.get(anyString())).thenReturn(stringPath);
        when(root.join(anyString())).thenReturn(join);
        when(join.get(anyString())).thenReturn(stringPath);

        when(cb.isNotNull(any())).thenReturn(predicate);
        when(cb.isNull(any())).thenReturn(predicate);
        when(cb.equal(any(), any())).thenReturn(predicate);
        when(cb.notEqual(any(), any())).thenReturn(predicate);
        when(cb.like(any(), anyString())).thenReturn(predicate);
        when(cb.and(any(Predicate[].class))).thenReturn(predicate);
        when(cb.or(any(Predicate[].class))).thenReturn(predicate);
    }

    // Helper para ejecutar toPredicate
    @SuppressWarnings("null")
    private Predicate executeSpec(String queryStr, String gender, Integer munId, Integer parishId,
                                  Integer artCatId, Integer artDiscId, Boolean hasDisability, Boolean hasIllness) {
        return CultorSpecification.withFilters(queryStr, gender, munId, parishId, artCatId, artDiscId, hasDisability, hasIllness)
                                  .toPredicate(root, query, cb);
    }

    // ---------------- Parameterized Tests ---------------- //

    @ParameterizedTest
    @CsvSource({
        "juan",           // Single word query
        "juan perez"      // Two words query
    })
    void testQuery(String searchQuery) {
        executeSpec(searchQuery, null, null, null, null, null, null, null);
        verify(cb, atLeastOnce()).like(any(), contains(searchQuery.split(" ")[0]));
    }

    @ParameterizedTest
    @CsvSource({
        "M",
        "F"
    })
    void testGenderFilter(String gender) {
        executeSpec(null, gender, null, null, null, null, null, null);
        verify(cb, atLeastOnce()).equal(stringPath, gender);
    }

    @ParameterizedTest
    @CsvSource({
        "1,2",   // municipalityId, parishId
        "3,4"    // artCategoryId, artDisciplineId
    })
    void testJoins(Integer firstId, Integer secondId) {
        // municipality/parish case
        executeSpec(null, null, firstId, secondId, null, null, null, null);
        verify(root, atLeastOnce()).join("municipality");
        verify(root, atLeastOnce()).join("parish");
        verify(cb, atLeastOnce()).equal(stringPath, firstId);
        verify(cb, atLeastOnce()).equal(stringPath, secondId);

        // artCategory/artDiscipline case
        executeSpec(null, null, null, null, firstId, secondId, null, null);
        verify(root, atLeastOnce()).join("artCategory");
        verify(root, atLeastOnce()).join("artDiscipline");
        verify(cb, atLeastOnce()).equal(stringPath, firstId);
        verify(cb, atLeastOnce()).equal(stringPath, secondId);
    }

    @ParameterizedTest
    @CsvSource({
        "true,false",   // hasDisability=true, hasIllness=false
        "false,true",   // hasDisability=false, hasIllness=true
        "false,false",  // both false
        "true,true"     // both true
    })
    void testBooleanFilters(Boolean hasDisability, Boolean hasIllness) {
        Predicate result = executeSpec(null, null, null, null, null, null, hasDisability, hasIllness);
        assertNotNull(result);

        if (Boolean.TRUE.equals(hasDisability)) {
            verify(cb, atLeastOnce()).isNotNull(stringPath);
            verify(cb, atLeastOnce()).notEqual(stringPath, "");
            verify(cb, atLeastOnce()).and(any(Predicate[].class));
        }

        if (Boolean.TRUE.equals(hasIllness)) {
            verify(cb, atLeastOnce()).isNotNull(stringPath);
            verify(cb, atLeastOnce()).notEqual(stringPath, "");
            verify(cb, atLeastOnce()).and(any(Predicate[].class));
        }
    }
}
