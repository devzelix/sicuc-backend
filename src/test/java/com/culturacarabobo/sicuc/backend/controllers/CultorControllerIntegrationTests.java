package com.culturacarabobo.sicuc.backend.controllers;

import org.junit.jupiter.api.BeforeEach; // Changed from org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.culturacarabobo.sicuc.backend.dtos.CultorRequest;
import com.culturacarabobo.sicuc.backend.entities.ArtCategory;
import com.culturacarabobo.sicuc.backend.entities.ArtDiscipline;
import com.culturacarabobo.sicuc.backend.entities.Cultor;
import com.culturacarabobo.sicuc.backend.entities.Municipality;
import com.culturacarabobo.sicuc.backend.entities.Parish;
import com.culturacarabobo.sicuc.backend.repositories.ArtCategoryRepository;
import com.culturacarabobo.sicuc.backend.repositories.ArtDisciplineRepository;
import com.culturacarabobo.sicuc.backend.repositories.CultorRepository;
import com.culturacarabobo.sicuc.backend.repositories.MunicipalityRepository;
import com.culturacarabobo.sicuc.backend.repositories.ParishRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.allOf;

import java.time.LocalDate;

/**
 * Integration tests for the {@link CultorController}.
 * <p>
 * This class uses {@link SpringBootTest} to load the full application context
 * and {@link MockMvc} to send real HTTP requests to the controller endpoints.
 * <p>
 * It leverages an in-memory H2 database (configured in
 * {@code src/test/resources/application.properties})
 * for a clean data environment on every run.
 * <p>
 * {@link AutoConfigureMockMvc(addFilters = false)} disables Spring Security JWT
 * filters for these tests.
 * <p>
 * {@link Transactional} ensures that each test method runs in its own
 * transaction, which is rolled back at the end, providing test isolation.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false) // Disables JWT security filters for this test class
@Transactional // Rolls back database changes after each test
public class CultorControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Repositories are injected to set up test data directly in the H2 database
    @Autowired
    private CultorRepository cultorRepository;
    @Autowired
    private MunicipalityRepository municipalityRepository;
    @Autowired
    private ParishRepository parishRepository;
    @Autowired
    private ArtCategoryRepository artCategoryRepository;
    @Autowired
    private ArtDisciplineRepository artDisciplineRepository;

    /**
     * Configures the {@link ObjectMapper} to correctly serialize/deserialize
     * {@link LocalDate} objects before each test runs.
     */
    @BeforeEach // Use org.junit.jupiter.api.BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    // ----------------------------------------------------------------
    // GET /cultors/{id} TESTS
    // ----------------------------------------------------------------

    /**
     * Test (Happy Path): GET /cultors/{id}
     * <p>
     * Scenario: A cultor with the requested ID exists.
     * <p>
     * Expected: HTTP 200 (OK) and the correct {@link CultorResponse} JSON body.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCultorExists_shouldReturn200OK() throws Exception {
        // --- 1. ARRANGE ---
        // Seed the H2 database with necessary related entities
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));

        // Seed the target cultor
        Cultor cultorPrueba = new Cultor(
                "Jose Ángel de Jesus", "Solett Bustamante", "M", "V-31456615",
                LocalDate.of(2003, 11, 20), "0424-4125472", null, null,
                m, p, "Urb. Las Acacias", ac, ad, null, 17, null, null, null
        );
        Cultor savedCultor = cultorRepository.save(cultorPrueba);
        Integer cultorId = savedCultor.getId();

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(get("/cultors/" + cultorId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cultorId))
                .andExpect(jsonPath("$.firstName").value("Jose Ángel de Jesus"))
                .andExpect(jsonPath("$.lastName").value("Solett Bustamante"));
    }

    /**
     * Test (Sad Path): GET /cultors/{id}
     * <p>
     * Scenario: No cultor exists with the requested ID.
     * <p>
     * Expected: HTTP 404 (Not Found) and an error message.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCultorNotFound_shouldReturn404() throws Exception {
        // --- 1. ARRANGE ---
        Integer idQueNoExiste = 9999;

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(get("/cultors/" + idQueNoExiste)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cultor Not Found With Id: " + idQueNoExiste));
    }

    // ----------------------------------------------------------------
    // POST /cultors TESTS
    // ----------------------------------------------------------------

    /**
     * Test (Happy Path): POST /cultors
     * <p>
     * Scenario: A valid {@link CultorRequest} is provided.
     * <p>
     * Expected: HTTP 201 (Created), a 'Location' header, and the new
     * {@link CultorResponse} JSON body.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCreateIsValid_shouldReturn201Created() throws Exception {
        // --- 1. ARRANGE ---
        // Seed dependencies
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));

        // Create the DTO to send
        CultorRequest cultorRequest = new CultorRequest(
                "Nuevo", "Cultor", "M", "V-99999999",
                LocalDate.of(1990, 1, 1), "0412-1234567",
                "nuevo@cultor.com", null,
                m.getId(), p.getId(), "Direccion nueva",
                ac.getId(), ad.getId(), null,
                10, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorRequest);

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(post("/cultors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Nuevo"))
                .andExpect(jsonPath("$.idNumber").value("V-99999999"));
    }

    /**
     * Test (Sad Path): POST /cultors
     * <p>
     * Scenario: The {@link CultorRequest} contains an 'idNumber' that already
     * exists.
     * <p>
     * Expected: HTTP 409 (Conflict) and an error message.
     */
    @SuppressWarnings("null")
    @Test
    public void whenIdNumberIsDuplicated_shouldReturn409() throws Exception {
        // --- 1. ARRANGE ---
        // Seed dependencies and an existing cultor with ID "V-111"
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        Cultor cultorExistente = new Cultor("Existente", "Cultor", "F", "V-111",
                LocalDate.of(1980, 5, 5), "0414-1111111", null, null,
                m, p, "Casa 1", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultorExistente);

        // Create a new DTO that attempts to use the same ID "V-111"
        CultorRequest cultorDuplicadoRequest = new CultorRequest(
                "Nuevo", "Cultor", "M", "V-111", // <-- Duplicate ID
                LocalDate.of(1990, 1, 1), "0412-1234567",
                "nuevo@cultor.com", null,
                m.getId(), p.getId(), "Direccion nueva",
                ac.getId(), ad.getId(), null,
                10, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorDuplicadoRequest);

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(post("/cultors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Id Number Already Exists"));
    }

    /**
     * Test (Sad Path): POST /cultors
     * <p>
     * Scenario: The {@link CultorRequest} fails Jakarta Validation (e.g., blank
     * fields, invalid patterns).
     * <p>
     * Expected: HTTP 400 (Bad Request) and a combined error message.
     */
    @SuppressWarnings("null")
    @Test
    public void whenDTOValidationFails_shouldReturn400() throws Exception {
        // --- 1. ARRANGE ---
        // Create an invalid DTO
        CultorRequest cultorInvalidoRequest = new CultorRequest(
                "", // <-- Fails @NotBlank
                "Cultor", "M", "V-CEDULA-MALA", // <-- Fails @Pattern and @Size
                LocalDate.of(1990, 1, 1), "0412-1234567",
                "nuevo@cultor.com", null,
                1, 1, "Direccion nueva",
                1, 1, null,
                10, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorInvalidoRequest);

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(post("/cultors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", allOf( // Verify all errors are present
                        containsString("Firstname Is Required"),
                        containsString("Idnumber Is Invalid"),
                        containsString("Idnumber Must Have A Maximum Of 10 Characters")
                )));
    }

    // ----------------------------------------------------------------
    // PUT /cultors/{id} TESTS
    // ----------------------------------------------------------------

    /**
     * Test (Happy Path): PUT /cultors/{id}
     * <p>
     * Scenario: A valid {@link CultorRequest} is provided for an existing cultor.
     * <p>
     * Expected: HTTP 200 (OK) and the updated {@link CultorResponse} JSON body.
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdateIsValid_shouldReturn200OK() throws Exception {
        // --- 1. ARRANGE ---
        // Seed an existing cultor to be updated
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        Cultor cultorExistente = new Cultor("Nombre", "Viejo", "F", "V-100",
                LocalDate.of(1980, 1, 1), "0414-1111111", null, null,
                m, p, "Casa Vieja", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultorExistente);
        Integer cultorId = cultorExistente.getId();

        // Create the DTO with updated data (respecting immutable fields)
        CultorRequest cultorActualizadoRequest = new CultorRequest(
                "Nombre", "Nuevo", // Last name changed
                "F", "V-100", // Immutable ID
                LocalDate.of(1980, 1, 1), // Immutable Birth Date
                "0414-2222222", // Phone changed
                "update@test.com", null, // Email changed
                m.getId(), p.getId(), "Casa Nueva", // Address changed
                ac.getId(), ad.getId(), null,
                21, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorActualizadoRequest);

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(put("/cultors/" + cultorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cultorId))
                .andExpect(jsonPath("$.lastName").value("Nuevo"))
                .andExpect(jsonPath("$.homeAddress").value("Casa Nueva"));
    }

    /**
     * Test (Sad Path): PUT /cultors/{id}
     * <p>
     * Scenario: The ID in the URL does not exist.
     * <p>
     * Expected: HTTP 404 (Not Found).
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdatingNonExistentCultor_shouldReturn404() throws Exception {
        // --- 1. ARRANGE ---
        CultorRequest cultorRequest = new CultorRequest(
                "Nombre", "Nuevo", "F", "V-100", LocalDate.of(1980, 1, 1),
                "0414-2222222", "update@test.com", null,
                1, 1, "Casa Nueva", 1, 1, null, 21, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorRequest);

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(put("/cultors/9999") // Non-existent ID
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cultor Not Found With Id: 9999"));
    }

    /**
     * Test (Sad Path): PUT /cultors/{id}
     * <p>
     * Scenario: The request attempts to change an immutable field (idNumber).
     * <p>
     * Expected: HTTP 400 (Bad Request) and an error message.
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdatingImmutable_idNumber_shouldReturn400() throws Exception {
        // --- 1. ARRANGE ---
        // Seed an existing cultor
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        Cultor cultorExistente = new Cultor("Nombre", "Viejo", "F", "V-100",
                LocalDate.of(1980, 1, 1), "0414-1111111", null, null,
                m, p, "Casa Vieja", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultorExistente);
        Integer cultorId = cultorExistente.getId();

        // Create a DTO that maliciously tries to change the ID number
        CultorRequest cultorMaliciosoRequest = new CultorRequest(
                "Nombre", "Nuevo", "F", "V-200", // <-- Illegal change!
                LocalDate.of(1980, 1, 1), "0414-2222222", "update@test.com", null,
                m.getId(), p.getId(), "Casa Nueva", ac.getId(), ad.getId(), null,
                21, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorMaliciosoRequest);

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(put("/cultors/" + cultorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("The IdNumber (Cédula) cannot be modified."));
    }

    /**
     * Test (Sad Path): PUT /cultors/{id}
     * <p>
     * Scenario: The request attempts to use a phone number that *another* cultor
     * already owns.
     * <p>
     * Expected: HTTP 409 (Conflict).
     */
    @SuppressWarnings("null")
    @Test
    public void whenPhoneNumberIsDuplicated_shouldReturn409() throws Exception {
        // --- 1. ARRANGE ---
        // Seed dependencies
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        
        // Seed Cultor A (the one we will update)
        Cultor cultorA = new Cultor("Cultor", "A", "F", "V-100",
                LocalDate.of(1980, 1, 1), "0414-1111111", null, null,
                m, p, "Casa A", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultorA);
        Integer cultorA_Id = cultorA.getId();

        // Seed Cultor B (the owner of the phone number we want to "steal")
        Cultor cultorB = new Cultor("Cultor", "B", "M", "V-200",
                LocalDate.of(1981, 1, 1), "0414-8888888", "b@test.com", null,
                m, p, "Casa B", ac, ad, null, 22, null, null, null);
        cultorRepository.save(cultorB);

        // Create a DTO to update Cultor A, but using Cultor B's phone
        CultorRequest cultorActualizadoRequest = new CultorRequest(
                "Cultor", "A-Modificado", "F", "V-100",
                LocalDate.of(1980, 1, 1), "0414-8888888", // <-- Cultor B's phone!
                "a@test.com", null,
                m.getId(), p.getId(), "Casa A Nueva",
                ac.getId(), ad.getId(), null,
                21, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorActualizadoRequest);

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(put("/cultors/" + cultorA_Id) // Updating Cultor A
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Phone Number Already Exists"));
    }

    /**
     * Test (Sad Path): PUT /cultors/{id}
     * <p>
     * Scenario: The {@link CultorRequest} body fails Jakarta Validation.
     * <p>
     * Expected: HTTP 400 (Bad Request) and combined error messages.
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdatingWithInvalidDTO_shouldReturn400() throws Exception {
        // --- 1. ARRANGE ---
        // Seed a cultor so the ID exists
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        Cultor cultor = new Cultor("Cultor", "Existente", "F", "V-100",
                LocalDate.of(1980, 1, 1), "0414-1111111", null, null,
                m, p, "Casa A", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultor);
        Integer cultorId = cultor.getId();

        // Create an invalid DTO (blank name, bad phone pattern)
        CultorRequest cultorInvalidoRequest = new CultorRequest(
                "", // <-- Fails @NotBlank
                "Nuevo Apellido", "F", "V-100", LocalDate.of(1980, 1, 1),
                "123", // <-- Fails @Pattern
                "update@test.com", null,
                m.getId(), p.getId(), "Casa Nueva", ac.getId(), ad.getId(), null,
                21, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorInvalidoRequest);

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(put("/cultors/" + cultorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", allOf(
                        containsString("Firstname Is Required"),
                        containsString("Phonenumber Is Invalid")
                )));
    }

    // ----------------------------------------------------------------
    // DELETE /cultors/{id} TESTS
    // ----------------------------------------------------------------

    /**
     * Test (Happy Path): DELETE /cultors/{id}
     * <p>
     * Scenario: The cultor with the requested ID exists.
     * <p>
     * Expected: HTTP 204 (No Content).
     */
    @Test
    public void whenDeleteIsValid_shouldReturn204NoContent() throws Exception {
        // --- 1. ARRANGE ---
        // Seed a cultor just to be deleted
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        Cultor cultorParaBorrar = new Cultor("Para", "Borrar", "F", "V-12345",
                LocalDate.of(1980, 1, 1), "0414-1234567", null, null,
                m, p, "Casa Borrada", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultorParaBorrar);
        Integer cultorId = cultorParaBorrar.getId();

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(delete("/cultors/" + cultorId))
                .andExpect(status().isNoContent());
    }

    /**
     * Test (Sad Path): DELETE /cultors/{id}
     * <p>
     * Scenario: No cultor exists with the requested ID.
     * <p>
     * Expected: HTTP 404 (Not Found) and an error message.
     */
    @Test
    public void whenDeletingNonExistentCultor_shouldReturn404() throws Exception {
        // --- 1. ARRANGE ---
        Integer idQueNoExiste = 9999;

        // --- 2. ACT & 3. ASSERT ---
        mockMvc.perform(delete("/cultors/" + idQueNoExiste))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cultor Not Found With Id: " + idQueNoExiste));
    }
}