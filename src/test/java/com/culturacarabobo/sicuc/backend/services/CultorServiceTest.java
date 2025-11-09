package com.culturacarabobo.sicuc.backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.culturacarabobo.sicuc.backend.dtos.CultorRequest;
import com.culturacarabobo.sicuc.backend.dtos.CultorResponse;
import com.culturacarabobo.sicuc.backend.entities.ArtCategory;
import com.culturacarabobo.sicuc.backend.entities.ArtDiscipline;
import com.culturacarabobo.sicuc.backend.entities.Cultor;
import com.culturacarabobo.sicuc.backend.entities.Municipality;
import com.culturacarabobo.sicuc.backend.entities.Parish;
import com.culturacarabobo.sicuc.backend.exceptions.DuplicateEntityException;
import com.culturacarabobo.sicuc.backend.repositories.ArtCategoryRepository;
import com.culturacarabobo.sicuc.backend.repositories.ArtDisciplineRepository;
import com.culturacarabobo.sicuc.backend.repositories.CultorRepository;
import com.culturacarabobo.sicuc.backend.repositories.MunicipalityRepository;
import com.culturacarabobo.sicuc.backend.repositories.ParishRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Unit tests for the {@link CultorService}.
 * <p>
 * This class uses Mockito to test the business logic in isolation by simulating
 * (mocking) the behavior of external dependencies (repositories).
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CultorServiceTest {

    // --- Mocks (Dependencies) ---
    @Mock
    private CultorRepository cultorRepository;
    @Mock
    private MunicipalityRepository municipalityRepository;
    @Mock
    private ParishRepository parishRepository;
    @Mock
    private ArtCategoryRepository artCategoryRepository;
    @Mock
    private ArtDisciplineRepository artDisciplineRepository;

    // --- Class Under Test (Injects the Mocks above) ---
    @InjectMocks
    private CultorService cultorService;

    // ----------------------------------------------------------------
    // GET and CREATE HAPPY PATHS
    // ----------------------------------------------------------------

    /**
     * Test (Happy Path): {@link CultorService#getById(Integer)}.
     * Scenario: Cultor exists in the database.
     * Expected: HTTP 200 OK and correct DTO.
     */
    @SuppressWarnings("null")
    @Test
    public void whenGetById_CultorExists_shouldReturn200OK() {
        // [ARRANGE] Setup mocks to return a fake existing Cultor...
        // ... (data creation logic omitted for brevity) ...
        Municipality m = new Municipality("Valencia"); m.setId(1);
        Parish p = new Parish("San José", m); p.setId(1);
        ArtCategory ac = new ArtCategory("Música"); ac.setId(1);
        ArtDiscipline ad = new ArtDiscipline("Guitarra", ac); ad.setId(1);
        Cultor fakeCultor = new Cultor("Jose", "Solett", "M", "V-123",
            LocalDate.of(2000, 1, 1), "0414-123", null, null, m, p, "Direccion", ac, ad, null, 10, null, null, null);
        fakeCultor.setId(1);
        when(cultorRepository.findById(1)).thenReturn(Optional.of(fakeCultor));

        // [ACT] Call the real service method
        ResponseEntity<CultorResponse> response = cultorService.getById(1);

        // [ASSERT] Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Jose", response.getBody().getFirstName());
    }

    /**
     * Test (Sad Path): {@link CultorService#getById(Integer)}.
     * Scenario: Cultor does not exist.
     * Expected: Throws {@link EntityNotFoundException}.
     */
    @Test
    public void whenGetById_CultorNotFound_shouldThrowEntityNotFound() {
        // [ARRANGE] Setup mock to return empty Optional
        when(cultorRepository.findById(anyInt())).thenReturn(Optional.empty());

        // [ACT & ASSERT] Verify the service throws the correct exception
        assertThrows(EntityNotFoundException.class, () -> {
            cultorService.getById(99);
        });
    }

    /**
     * Test (Sad Path): {@link CultorService#create(CultorRequest)}.
     * Scenario: The request contains an ID Number that already exists.
     * Expected: Throws {@link DuplicateEntityException}.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCreate_IdNumberIsDuplicated_shouldThrowDuplicateEntityException() {
        // [ARRANGE] Setup mock to check for duplication
        CultorRequest requestDto = new CultorRequest("Nuevo", "Cultor", "M", "V-123", LocalDate.of(1990, 1, 1), "0412-1234567", "nuevo@cultor.com", null, 1, 1, "Dir", 1, 1, null, 10, null, null, null);
        when(cultorRepository.existsByIdNumber("V-123")).thenReturn(true);

        // [ACT & ASSERT] Verify the service throws the correct exception
        assertThrows(DuplicateEntityException.class, () -> {
            cultorService.create(requestDto);
        });
        // Verify that save was never called
        verify(cultorRepository, never()).save(any(Cultor.class));
    }
    
    /**
     * Test (Happy Path): {@link CultorService#create(CultorRequest)}.
     * Scenario: All data is valid and unique.
     * Expected: HTTP 201 Created and save is called once.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCreateIsValid_shouldReturn201Created() {
        // [ARRANGE] Setup all mocks for success
        Municipality m = new Municipality("Valencia"); m.setId(1);
        Parish p = new Parish("San José", m); p.setId(1);
        ArtCategory ac = new ArtCategory("Música"); ac.setId(1);
        ArtDiscipline ad = new ArtDiscipline("Guitarra", ac); ad.setId(1);
        Cultor cultorGuardado = new Cultor("Nuevo", "Cultor", "M", "V-12345", LocalDate.of(1990, 1, 1), "0412-1234567", "nuevo@cultor.com", null, m, p, "Direccion Nueva", ac, ad, null, 10, null, null, null);
        cultorGuardado.setId(1);
        
        CultorRequest requestDto = new CultorRequest("Nuevo", "Cultor", "M", "V-12345", LocalDate.of(1990, 1, 1), "0412-1234567", "nuevo@cultor.com", null, 1, 1, "Direccion nueva", 1, 1, null, 10, null, null, null);

        when(cultorRepository.existsByIdNumber(anyString())).thenReturn(false);
        when(cultorRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(cultorRepository.existsByEmail(anyString())).thenReturn(false);
        when(municipalityRepository.findById(1)).thenReturn(Optional.of(m));
        when(parishRepository.findById(1)).thenReturn(Optional.of(p));
        when(artCategoryRepository.findById(1)).thenReturn(Optional.of(ac));
        when(artDisciplineRepository.findById(1)).thenReturn(Optional.of(ad));
        when(cultorRepository.save(any(Cultor.class))).thenReturn(cultorGuardado);

        // [ACT]
        ResponseEntity<CultorResponse> response = cultorService.create(requestDto);

        // [ASSERT]
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cultorRepository, times(1)).save(any(Cultor.class));
    }


    // ----------------------------------------------------------------
    // UPDATE and DELETE SAD PATHS
    // ----------------------------------------------------------------

    /**
     * Test (Sad Path): {@link CultorService#create(CultorRequest)}.
     * Scenario: The request contains a phone number that already exists.
     * Expected: Throws {@link DuplicateEntityException}.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCreate_PhoneNumberIsDuplicated_shouldThrowDuplicateEntityException() {
        // [ARRANGE] Setup mock for duplication check
        CultorRequest requestDto = new CultorRequest("Nombre", "Cultor", "M", "V-123", LocalDate.of(1990, 1, 1), "0412-1111111", "nuevo@mail.com", null, 1, 1, "Dir", 1, 1, null, 10, null, null, null);
        when(cultorRepository.existsByPhoneNumber("0412-1111111")).thenReturn(true);

        // [ACT & ASSERT]
        assertThrows(DuplicateEntityException.class, () -> {
            cultorService.create(requestDto);
        });
        verify(cultorRepository, never()).save(any(Cultor.class));
    }


    /**
     * Test (Sad Path): {@link CultorService#create(CultorRequest)}.
     * Scenario: Parish ID and Municipality ID do not match the business rule
     * (Parish does not belong to Municipality).
     * Expected: Throws {@link IllegalArgumentException}.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCreate_ParishDoesNotBelongToMunicipality_shouldThrowIllegalArgument() {
        // [ARRANGE] Setup mocks for conflicting IDs
        Integer municipalityId = 1;
        Integer parishId = 99; // ID conflict
        CultorRequest requestDto = new CultorRequest("Nombre", "Cultor", "M", "V-123", LocalDate.of(1990, 1, 1), "0412-1111111", "test@mail.com", null, municipalityId, parishId, "Dir", 1, 1, null, 10, null, null, null);
        
        // Mock related entities (Parish 99 belongs to Municipality 2, but request sent Municipality 1)
        Municipality m = new Municipality("Valencia"); m.setId(1);
        Municipality m2 = new Municipality("Otro"); m2.setId(2);
        Parish p = new Parish("San José", m2); p.setId(99); 

        when(cultorRepository.existsByIdNumber(anyString())).thenReturn(false);
        when(cultorRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(cultorRepository.existsByEmail(anyString())).thenReturn(false);
        when(municipalityRepository.findById(municipalityId)).thenReturn(Optional.of(m));
        when(parishRepository.findById(parishId)).thenReturn(Optional.of(p)); // Returns the conflicting parish

        // [ACT & ASSERT]
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cultorService.create(requestDto);
        });

        assertEquals("The Selected Parish Does Not Belong To The Chosen Municipality", exception.getMessage());
        verify(cultorRepository, never()).save(any(Cultor.class));
    }


    /**
     * Test (Sad Path): {@link CultorService#update(Integer, CultorRequest)}.
     * Scenario: Attempts to change the immutable ID Number field.
     * Expected: Throws {@link IllegalArgumentException}.
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdatingImmutableIdNumber_shouldThrowIllegalArgumentException() {
        // [ARRANGE] Setup mocks with conflicting DTO
        Integer cultorId = 1;
        CultorRequest requestDto = new CultorRequest("Nombre", "Actualizado", "M", "V-666", LocalDate.of(1990, 1, 1), "0412-9999999", null, null, 1, 1, "Direccion", 1, 1, null, 11, null, null, null);
        Cultor cultorExistente = new Cultor("Nombre", "Viejo", "M", "V-12345", LocalDate.of(1990, 1, 1), "0412-1111111", null, null, null, null, "Dir", null, null, null, 10, null, null, null);
        cultorExistente.setId(cultorId);
        when(cultorRepository.findById(cultorId)).thenReturn(Optional.of(cultorExistente));

        // [ACT & ASSERT]
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cultorService.update(cultorId, requestDto);
        });

        assertEquals("The IdNumber (Cédula) cannot be modified.", exception.getMessage());
        verify(cultorRepository, never()).save(any(Cultor.class));
    }

    /**
     * Test (Sad Path): {@link CultorService#update(Integer, CultorRequest)}.
     * Scenario: Attempts to use a phone number that *another* cultor already owns.
     * Expected: Throws {@link DuplicateEntityException}.
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdate_PhoneNumberIsDuplicatedByOtherCultor_shouldThrowDuplicateException() {
        // [ARRANGE] Setup mocks for unique check exclusion
        Integer cultorId = 1;
        CultorRequest requestDto = new CultorRequest("Nombre", "Actualizado", "M", "V-12345", LocalDate.of(1990, 1, 1), "0412-9999999", "update@cultor.com", null, 1, 1, "Dir", 1, 1, null, 11, null, null, null);
        Cultor cultorExistente = new Cultor("Nombre", "Viejo", "M", "V-12345", LocalDate.of(1990, 1, 1), "0412-1111111", null, null, null, null, "Dir Vieja", null, null, null, 10, null, null, null);
        cultorExistente.setId(cultorId);

        when(cultorRepository.findById(cultorId)).thenReturn(Optional.of(cultorExistente));
        when(cultorRepository.existsByPhoneNumberAndIdNot("0412-9999999", cultorId)).thenReturn(true);
        
        // [ACT & ASSERT]
        assertThrows(DuplicateEntityException.class, () -> {
            cultorService.update(cultorId, requestDto);
        });
        verify(cultorRepository, never()).save(any(Cultor.class));
    }

    /**
     * Test (Happy Path): {@link CultorService#delete(Integer)}.
     * Scenario: Cultor exists and is successfully deleted.
     * Expected: HTTP 204 No Content.
     */
    @Test
    public void whenDeleteIsValid_shouldReturn204NoContent() {
        // [ARRANGE] Setup mock for successful deletion
        Integer cultorId = 1;
        when(cultorRepository.existsById(cultorId)).thenReturn(true);
        doNothing().when(cultorRepository).deleteById(cultorId);

        // [ACT & ASSERT]
        ResponseEntity<Void> response = cultorService.delete(cultorId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cultorRepository, times(1)).deleteById(cultorId);
    }
}