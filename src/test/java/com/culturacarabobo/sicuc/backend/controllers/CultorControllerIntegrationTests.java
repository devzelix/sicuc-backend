package com.culturacarabobo.sicuc.backend.controllers;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
// --- ¡CAMBIO 1: Desactiva los filtros de seguridad (JWT) para esta prueba! ---
@AutoConfigureMockMvc(addFilters = false) 
@Transactional
public class CultorControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

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
    @Autowired
    private ObjectMapper objectMapper;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // Registra el módulo para que ObjectMapper sepa cómo (de)serializar LocalDate
        objectMapper.registerModule(new JavaTimeModule());
    }

    @SuppressWarnings("null")
    @Test
    public void whenCultorExists_shouldReturn200OK() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Guardamos las dependencias en la BD H2 (que ahora sí se usará)
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));

        Cultor cultorPrueba = new Cultor(
            "Jose Ángel de Jesus", "Solett Bustamante", "M", "V-31456615",
            LocalDate.of(2003, 11, 20), "0424-4125472", null, null,
            m, p, "Urb. Las Acacias", ac, ad, null, 17, null, null, null
        );

        Cultor savedCultor = cultorRepository.save(cultorPrueba);
        Integer cultorId = savedCultor.getId();

        // --- 2. ACT ---
        mockMvc.perform(get("/cultors/" + cultorId)
                .contentType(MediaType.APPLICATION_JSON))

                // --- 3. ASSERT ---
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cultorId))
                // --- ¡AJUSTA ESTO a lo que tu StringUtils.toCapitalize() realmente haga! ---
                .andExpect(jsonPath("$.firstName").value("Jose Ángel de Jesus")) 
                .andExpect(jsonPath("$.lastName").value("Solett Bustamante"));
    }

    @SuppressWarnings("null")
    @Test
    public void whenCultorNotFound_shouldReturn404() throws Exception {
        // ... (Arrange) ...
        Integer idQueNoExiste = 999;

        // ... (Act) ...
        mockMvc.perform(get("/cultors/" + idQueNoExiste)
                .contentType(MediaType.APPLICATION_JSON))

        // ... (Assert) ...
                // ¡Ahora SÍ dará 404, porque la seguridad está apagada!
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cultor Not Found With Id: 999"));
    }

    /**
     * Prueba el "camino feliz": POST /cultors cuando los datos son válidos.
     * Espera un status 201 Created y el JSON del nuevo cultor.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCreateIsValid_shouldReturn201Created() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Creamos las dependencias (igual que en la prueba GET)
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));

        // Creamos el DTO (CultorRequest) que enviaremos
        CultorRequest cultorRequest = new CultorRequest(
            "Nuevo", "Cultor", "M", "V-99999999",
            LocalDate.of(1990, 1, 1), "0412-1234567",
            "nuevo@cultor.com", null,
            m.getId(), p.getId(), "Direccion nueva",
            ac.getId(), ad.getId(), null,
            10, null, null, null
        );

        // Convertimos el DTO a un string JSON
        String cultorJson = objectMapper.writeValueAsString(cultorRequest);

        // --- 2. ACT ---
        mockMvc.perform(post("/cultors") // Usamos post()
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson)) // Añadimos el body

                // --- 3. ASSERT ---
                .andExpect(status().isCreated()) // Esperamos 201 Created
                .andExpect(header().exists("Location")) // Esperamos la cabecera "Location"
                .andExpect(jsonPath("$.id").exists()) // Verificamos que la respuesta tenga un ID
                .andExpect(jsonPath("$.firstName").value("Nuevo"))
                .andExpect(jsonPath("$.idNumber").value("V-99999999"));
    }

    /**
     * Prueba el "camino triste": POST /cultors con una cédula duplicada.
     * Espera un status 409 Conflict.
     */
    @SuppressWarnings("null")
    @Test
    public void whenIdNumberIsDuplicated_shouldReturn409() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Creamos un cultor base con la cédula "V-111"
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));

        Cultor cultorExistente = new Cultor("Existente", "Cultor", "F", "V-111",
            LocalDate.of(1980, 5, 5), "0414-1111111", null, null,
            m, p, "Casa 1", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultorExistente);

        // Creamos un DTO NUEVO pero con la MISMA cédula "V-111"
        CultorRequest cultorDuplicadoRequest = new CultorRequest(
            "Nuevo", "Cultor", "M", "V-111", // <-- Cédula duplicada
            LocalDate.of(1990, 1, 1), "0412-1234567", // <-- Teléfono diferente
            "nuevo@cultor.com", null,
            m.getId(), p.getId(), "Direccion nueva",
            ac.getId(), ad.getId(), null,
            10, null, null, null
        );

        String cultorJson = objectMapper.writeValueAsString(cultorDuplicadoRequest);

        // --- 2. ACT ---
        mockMvc.perform(post("/cultors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))

                // --- 3. ASSERT ---
                .andExpect(status().isConflict()) // Esperamos 409 Conflict
                .andExpect(jsonPath("$.error").value("Id Number Already Exists"));
    }

    /**
     * Prueba el "camino triste": POST /cultors con datos de DTO inválidos.
     * Espera un status 400 Bad Request.
     */
    @SuppressWarnings("null")
    @Test
    public void whenDTOValidationFails_shouldReturn400() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Creamos un DTO con 'firstName' vacío y 'idNumber' con formato incorrecto
        CultorRequest cultorInvalidoRequest = new CultorRequest(
            "", // <-- Vacío (falla @NotBlank)
            "Cultor", "M", "V-CEDULA-MALA", // <-- Formato incorrecto (falla @Pattern)
            LocalDate.of(1990, 1, 1), "0412-1234567",
            "nuevo@cultor.com", null,
            1, 1, "Direccion nueva", // IDs falsos, no importa porque fallará antes
            1, 1, null,
            10, null, null, null
        );

        String cultorJson = objectMapper.writeValueAsString(cultorInvalidoRequest);

        // --- 2. ACT ---
        mockMvc.perform(post("/cultors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))

                // --- 3. ASSERT ---
                .andExpect(status().isBadRequest()) // Esperamos 400 Bad Request
                // Verificamos que el JSON de error contenga TODOS los mensajes esperados,
                // sin importar el orden o las comas.
                .andExpect(jsonPath("$.error", allOf(
                    containsString("Firstname Is Required"),
                    containsString("Idnumber Is Invalid"),
                    containsString("Idnumber Must Have A Maximum Of 10 Characters")
                )));
    }

    /**
     * Prueba el "camino feliz": PUT /cultors/{id} con datos válidos.
     * Espera un status 200 OK y el JSON del cultor actualizado.
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdateIsValid_shouldReturn200OK() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Creamos un cultor base para actualizar
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        
        Cultor cultorExistente = new Cultor("Nombre", "Viejo", "F", "V-100",
            LocalDate.of(1980, 1, 1), "0414-1111111", null, null,
            m, p, "Casa Vieja", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultorExistente);
        Integer cultorId = cultorExistente.getId();

        // Creamos el DTO (CultorRequest) con los datos actualizados
        // NOTA: Respetamos los campos inmutables (idNumber, birthDate)
        CultorRequest cultorActualizadoRequest = new CultorRequest(
            "Nombre", "Nuevo", // Apellido cambiado
            "F", "V-100", // Cédula (inmutable)
            LocalDate.of(1980, 1, 1), // Fecha Nac. (inmutable)
            "0414-2222222", // Teléfono cambiado
            "update@test.com", null, // Email cambiado
            m.getId(), p.getId(), "Casa Nueva", // Dirección cambiada
            ac.getId(), ad.getId(), null,
            21, null, null, null // Años de experiencia cambiados
        );

        String cultorJson = objectMapper.writeValueAsString(cultorActualizadoRequest);

        // --- 2. ACT ---
        mockMvc.perform(put("/cultors/" + cultorId) // Usamos put()
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson)) // Añadimos el body

                // --- 3. ASSERT ---
                .andExpect(status().isOk()) // Esperamos 200 OK
                .andExpect(jsonPath("$.id").value(cultorId))
                .andExpect(jsonPath("$.lastName").value("Nuevo")) // Verificamos el apellido
                .andExpect(jsonPath("$.homeAddress").value("Casa Nueva")); // Verificamos la dirección
    }

    /**
     * Prueba el "camino triste": PUT /cultors/{id} con un ID que no existe.
     * Espera un status 404 Not Found.
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdatingNonExistentCultor_shouldReturn404() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Creamos un DTO válido, pero usaremos un ID de URL que no existe
        CultorRequest cultorRequest = new CultorRequest(
            "Nombre", "Nuevo", "F", "V-100", LocalDate.of(1980, 1, 1),
            "0414-2222222", "update@test.com", null,
            1, 1, "Casa Nueva", 1, 1, null, 21, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorRequest);

        // --- 2. ACT ---
        mockMvc.perform(put("/cultors/9999") // ID que no existe
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))

                // --- 3. ASSERT ---
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cultor Not Found With Id: 9999"));
    }

    /**
     * Prueba el "camino triste": PUT /cultors/{id} intentando cambiar un campo inmutable.
     * Espera un status 400 Bad Request.
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdatingImmutable_idNumber_shouldReturn400() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Creamos un cultor base
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        
        Cultor cultorExistente = new Cultor("Nombre", "Viejo", "F", "V-100",
            LocalDate.of(1980, 1, 1), "0414-1111111", null, null,
            m, p, "Casa Vieja", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultorExistente);
        Integer cultorId = cultorExistente.getId();

        // Creamos un DTO que intenta cambiar la CÉDULA
        CultorRequest cultorMaliciosoRequest = new CultorRequest(
            "Nombre", "Nuevo", "F", "V-200", // <-- ¡Cédula cambiada!
            LocalDate.of(1980, 1, 1), "0414-2222222", "update@test.com", null,
            m.getId(), p.getId(), "Casa Nueva", ac.getId(), ad.getId(), null,
            21, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorMaliciosoRequest);

        // --- 2. ACT ---
        mockMvc.perform(put("/cultors/" + cultorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))

                // --- 3. ASSERT ---
                .andExpect(status().isBadRequest()) // Esperamos 400 Bad Request
                .andExpect(jsonPath("$.error").value("The IdNumber (Cédula) cannot be modified."));
    }

    /**
     * Prueba el "camino triste": PUT /cultors/{id} usando un campo único (mutable) de OTRO cultor.
     * Espera un status 409 Conflict.
     */
    @SuppressWarnings("null")
    @Test
    public void whenPhoneNumberIsDuplicated_shouldReturn409() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Necesitamos DOS cultores
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        
        // Cultor A (El que vamos a actualizar)
        Cultor cultorA = new Cultor("Cultor", "A", "F", "V-100",
            LocalDate.of(1980, 1, 1), "0414-1111111", null, null,
            m, p, "Casa A", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultorA);
        Integer cultorA_Id = cultorA.getId();

        // Cultor B (El dueño del teléfono que vamos a "robar")
        Cultor cultorB = new Cultor("Cultor", "B", "M", "V-200",
            LocalDate.of(1981, 1, 1), "0414-8888888", "b@test.com", null,
            m, p, "Casa B", ac, ad, null, 22, null, null, null);
        cultorRepository.save(cultorB);

        // Creamos un DTO para actualizar al Cultor A...
        // ...pero usando el NÚMERO DE TELÉFONO del Cultor B
        CultorRequest cultorActualizadoRequest = new CultorRequest(
            "Cultor", "A-Modificado", "F", "V-100", // Cédula (inmutable) OK
            LocalDate.of(1980, 1, 1), "0414-8888888", // <-- ¡Teléfono del Cultor B!
            "a@test.com", null,
            m.getId(), p.getId(), "Casa A Nueva",
            ac.getId(), ad.getId(), null,
            21, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorActualizadoRequest);

        // --- 2. ACT ---
        mockMvc.perform(put("/cultors/" + cultorA_Id) // Actualizando a Cultor A
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))

                // --- 3. ASSERT ---
                .andExpect(status().isConflict()) // Esperamos 409 Conflict
                .andExpect(jsonPath("$.error").value("Phone Number Already Exists"));
    }

    /**
     * Prueba el "camino triste": PUT /cultors/{id} con DTO inválido (campos vacíos).
     * Espera un status 400 Bad Request.
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdatingWithInvalidDTO_shouldReturn400() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Solo necesitamos un ID que exista
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        Cultor cultor = new Cultor("Cultor", "Existente", "F", "V-100",
            LocalDate.of(1980, 1, 1), "0414-1111111", null, null,
            m, p, "Casa A", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultor);
        Integer cultorId = cultor.getId();

        // Creamos un DTO inválido (nombre vacío, teléfono malo)
        CultorRequest cultorInvalidoRequest = new CultorRequest(
            "", // <-- Vacío (falla @NotBlank)
            "Nuevo Apellido", "F", "V-100", LocalDate.of(1980, 1, 1),
            "123", // <-- Formato inválido (falla @Pattern)
            "update@test.com", null,
            m.getId(), p.getId(), "Casa Nueva", ac.getId(), ad.getId(), null,
            21, null, null, null
        );
        String cultorJson = objectMapper.writeValueAsString(cultorInvalidoRequest);

        // --- 2. ACT ---
        mockMvc.perform(put("/cultors/" + cultorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cultorJson))

                // --- 3. ASSERT ---
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", allOf(
                    containsString("Firstname Is Required"),
                    containsString("Phonenumber Is Invalid")
                )));
    }

    /**
     * Prueba el "camino feliz": DELETE /cultors/{id} con un ID válido.
     * Espera un status 204 No Content.
     */
    @Test
    public void whenDeleteIsValid_shouldReturn204NoContent() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Creamos un cultor solo para que exista y podamos borrarlo
        Municipality m = municipalityRepository.save(new Municipality("Valencia"));
        Parish p = parishRepository.save(new Parish("San José", m));
        ArtCategory ac = artCategoryRepository.save(new ArtCategory("Música"));
        ArtDiscipline ad = artDisciplineRepository.save(new ArtDiscipline("Guitarra", ac));
        
        Cultor cultorParaBorrar = new Cultor("Para", "Borrar", "F", "V-12345",
            LocalDate.of(1980, 1, 1), "0414-1234567", null, null,
            m, p, "Casa Borrada", ac, ad, null, 20, null, null, null);
        cultorRepository.save(cultorParaBorrar);
        Integer cultorId = cultorParaBorrar.getId();

        // --- 2. ACT ---
        mockMvc.perform(delete("/cultors/" + cultorId)) // Usamos delete()

                // --- 3. ASSERT ---
                .andExpect(status().isNoContent()); // Esperamos 204 No Content

        // Verificación Opcional: nos aseguramos de que realmente se borró
        // assertFalse(cultorRepository.existsById(cultorId));
    }

    /**
     * Prueba el "camino triste": DELETE /cultors/{id} con un ID que no existe.
     * Espera un status 404 Not Found.
     */
    @Test
    public void whenDeletingNonExistentCultor_shouldReturn404() throws Exception {
        // --- 1. ARRANGE ---
        Integer idQueNoExiste = 9999;

        // --- 2. ACT ---
        mockMvc.perform(delete("/cultors/" + idQueNoExiste)) // ID que no existe

                // --- 3. ASSERT ---
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cultor Not Found With Id: 9999"));
    }
}