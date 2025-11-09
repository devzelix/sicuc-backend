package com.culturacarabobo.sicuc.backend.services;

import static org.junit.jupiter.api.Assertions.*; // Para asserts como assertEquals
import static org.mockito.Mockito.*; // Para 'when', 'verify'
import static org.mockito.ArgumentMatchers.any; // Para 'any(Cultor.class)'
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

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
 * Pruebas Unitarias para CultorService.
 * Estas pruebas se enfocan en la lógica de negocio DENTRO del servicio.
 * Todas las dependencias (como los repositorios) son "Mocks" (simulacros).
 */
@ExtendWith(MockitoExtension.class) // <-- Activa Mockito
@MockitoSettings(strictness = Strictness.LENIENT)
public class CultorServiceTest {

    // --- Dependencias Simuladas (Mocks) ---
    // Crea una versión "falsa" de cada repositorio.
    // No tienen conexión a la BD. Nosotros controlamos lo que devuelven.
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

    // --- Clase Bajo Prueba ---
    // Crea una instancia REAL de CultorService e inyecta
    // automáticamente todos los @Mocks de arriba en su constructor.
    @InjectMocks
    private CultorService cultorService;

    // ----------------------------------------------------------------
    // PRUEBAS PARA getById()
    // ----------------------------------------------------------------

    @SuppressWarnings("null")
    @Test
    public void whenGetById_CultorExists_shouldReturn200OK() {
        // --- 1. ARRANGE (Preparar) ---
        
        // 1.1 Crear los datos falsos que esperamos
        Municipality m = new Municipality("Valencia");
        m.setId(1); // Les ponemos IDs falsos
        Parish p = new Parish("San José", m);
        p.setId(1);
        ArtCategory ac = new ArtCategory("Música");
        ac.setId(1);
        ArtDiscipline ad = new ArtDiscipline("Guitarra", ac);
        ad.setId(1);
        
        Cultor fakeCultor = new Cultor("Jose", "Solett", "M", "V-123",
            LocalDate.of(2000, 1, 1), "0414-123", null, null,
            m, p, "Direccion", ac, ad, null, 10, null, null, null);
        fakeCultor.setId(1); // El ID del cultor

        // 1.2 ¡LA MAGIA DE MOCKITO!
        // Le decimos al repositorio FALSO: "Cuando CUALQUIERA llame a findById(1)..."
        when(cultorRepository.findById(1)).thenReturn(Optional.of(fakeCultor));
        // "...entonces DEVUELVE un Optional que contenga nuestro cultor falso."

        // --- 2. ACT (Actuar) ---
        // Llamamos al método REAL del servicio
        ResponseEntity<CultorResponse> response = cultorService.getById(1);

        // --- 3. ASSERT (Verificar) ---
        // Verificamos que la respuesta sea la correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jose", response.getBody().getFirstName());
        assertEquals(1, response.getBody().getMunicipalityId());
    }

    @Test
    public void whenGetById_CultorNotFound_shouldThrowEntityNotFound() {
        // --- 1. ARRANGE (Preparar) ---
        // Le decimos al repositorio FALSO: "Cuando llamen a findById con CUALQUIER ID..."
        when(cultorRepository.findById(anyInt())).thenReturn(Optional.empty());
        // "...entonces DEVUELVE un Optional vacío."

        // --- 2. ACT & 3. ASSERT ---
        // Verificamos que el servicio lanza la excepción correcta
        assertThrows(EntityNotFoundException.class, () -> {
            // Esta línea de código debe fallar con EntityNotFoundException
            cultorService.getById(99);
        });
    }

    // ----------------------------------------------------------------
    // PRUEBAS PARA create()
    // ----------------------------------------------------------------
    
    @SuppressWarnings("null")
    @Test
    public void whenCreate_IdNumberIsDuplicated_shouldThrowDuplicateEntityException() {
        // --- 1. ARRANGE (Preparar) ---
        
        // 1.1 Creamos un DTO (Request) para el nuevo cultor
        CultorRequest requestDto = new CultorRequest(
            "Nuevo", "Cultor", "M", "V-123", // Cédula "V-123"
            LocalDate.of(1990, 1, 1), "0412-1234567",
            "nuevo@cultor.com", null,
            1, 1, "Direccion nueva",
            1, 1, null,
            10, null, null, null
        );

        // 1.2 Configuración de Mockito (¡El Camino Triste!)
        // Le decimos al repo: "Cuando pregunten si 'V-123' existe..."
        when(cultorRepository.existsByIdNumber("V-123")).thenReturn(true);
        // "...responde que SÍ (true)."

        // --- 2. ACT & 3. ASSERT ---
        // Verificamos que el servicio detecta el duplicado y lanza la excepción
        assertThrows(DuplicateEntityException.class, () -> {
            cultorService.create(requestDto);
        });

        // Verificación Opcional: asegurarnos de que NUNCA se intentó guardar
        verify(cultorRepository, never()).save(any(Cultor.class));
    }

    /**
     * Prueba el "camino feliz" de create().
     * Verifica que si todas las validaciones pasan, el cultor se guarda
     * y se devuelve un 201 Created.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCreateIsValid_shouldReturn201Created() {
        // --- 1. ARRANGE (Preparar) ---

        // 1.1 - Creamos el DTO (Request)
        CultorRequest requestDto = new CultorRequest(
            "Nuevo", "Cultor", "M", "V-12345",
            LocalDate.of(1990, 1, 1), "0412-1234567",
            "nuevo@cultor.com", null,
            1, // municipalityId
            1, // parishId
            "Direccion nueva",
            1, // artCategoryId
            1, // artDisciplineId
            null, 10, null, null, null
        );

        // 1.2 - Creamos las entidades falsas que los repositorios deben "encontrar"
        Municipality m = new Municipality("Valencia"); m.setId(1);
        Parish p = new Parish("San José", m); p.setId(1);
        ArtCategory ac = new ArtCategory("Música"); ac.setId(1);
        ArtDiscipline ad = new ArtDiscipline("Guitarra", ac); ad.setId(1);

        // 1.3 - Creamos el cultor que el 'save' debe devolver
        Cultor cultorGuardado = new Cultor("Nuevo", "Cultor", "M", "V-12345",
            LocalDate.of(1990, 1, 1), "0412-1234567", "nuevo@cultor.com", null,
            m, p, "Direccion Nueva", ac, ad, null, 10, null, null, null);
        cultorGuardado.setId(1); // Le damos un ID
        cultorGuardado.setCreatedAt(LocalDate.now()); // Le damos fecha de creación

        // 1.4 - ¡Configuramos los Mocks!
        // Le decimos a Mockito qué debe devolver cada llamada al repositorio.

        // Validaciones de duplicados (todas devuelven 'false')
        when(cultorRepository.existsByIdNumber(anyString())).thenReturn(false);
        when(cultorRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(cultorRepository.existsByEmail(anyString())).thenReturn(false);

        // Búsqueda de entidades relacionadas (todas devuelven la entidad falsa)
        when(municipalityRepository.findById(1)).thenReturn(Optional.of(m));
        when(parishRepository.findById(1)).thenReturn(Optional.of(p));
        when(artCategoryRepository.findById(1)).thenReturn(Optional.of(ac));
        when(artDisciplineRepository.findById(1)).thenReturn(Optional.of(ad));

        // La llamada a 'save' (debe devolver el cultor falso)
        // Usamos any(Cultor.class) porque el objeto se crea DENTRO del método
        when(cultorRepository.save(any(Cultor.class))).thenReturn(cultorGuardado);

        // --- 2. ACT (Actuar) ---
        ResponseEntity<CultorResponse> response = cultorService.create(requestDto);

        // --- 3. ASSERT (Verificar) ---
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Verifica el 201
        assertNotNull(response.getBody());
        assertEquals("Nuevo", response.getBody().getFirstName());
        assertEquals("V-12345", response.getBody().getIdNumber());

        // Verificamos que el método 'save' fue llamado exactamente 1 vez
        verify(cultorRepository, times(1)).save(any(Cultor.class));
    }


    // ----------------------------------------------------------------
    // PRUEBAS PARA update()
    // ----------------------------------------------------------------

    @SuppressWarnings("null")
    @Test
    public void whenUpdateIsValid_shouldReturn200OK() {
        // --- 1. ARRANGE (Preparar) ---
        Integer cultorId = 1;

        // 1.1 - Creamos el DTO (Request) con los datos actualizados
        CultorRequest requestDto = new CultorRequest(
            "Nombre", "Actualizado", // Apellido cambiado
            "M", "V-12345", // Cédula (inmutable)
            LocalDate.of(1990, 1, 1), // Fecha Nac. (inmutable)
            "0412-9999999", // Teléfono cambiado
            "update@cultor.com", null,
            1, 1, "Direccion Actualizada", // Dirección cambiada
            1, 1, null, 11, null, null, null
        );

        // 1.2 - Creamos las entidades falsas base
        Municipality m = new Municipality("Valencia"); m.setId(1);
        Parish p = new Parish("San José", m); p.setId(1);
        ArtCategory ac = new ArtCategory("Música"); ac.setId(1);
        ArtDiscipline ad = new ArtDiscipline("Guitarra", ac); ad.setId(1);

        // 1.3 - Creamos el cultor que "ya existe" en la BD
        Cultor cultorExistente = new Cultor("Nombre", "Viejo", "M", "V-12345",
            LocalDate.of(1990, 1, 1), "0412-1111111", null, null,
            m, p, "Direccion Vieja", ac, ad, null, 10, null, null, null);
        cultorExistente.setId(cultorId);

        // 1.4 - Configuración de Mockito
        // Cuando busquen al cultor por ID, devuélvelo
        when(cultorRepository.findById(cultorId)).thenReturn(Optional.of(cultorExistente));
        
        // Validaciones de duplicados (devuelven 'false')
        when(cultorRepository.existsByPhoneNumberAndIdNot(anyString(), eq(cultorId))).thenReturn(false);
        when(cultorRepository.existsByEmailAndIdNot(anyString(), eq(cultorId))).thenReturn(false);
        
        // Búsqueda de entidades relacionadas (devuelven la entidad falsa)
        when(municipalityRepository.findById(1)).thenReturn(Optional.of(m));
        when(parishRepository.findById(1)).thenReturn(Optional.of(p));
        when(artCategoryRepository.findById(1)).thenReturn(Optional.of(ac));
        when(artDisciplineRepository.findById(1)).thenReturn(Optional.of(ad));

        // Cuando guarden el cultor, devuelve el mismo objeto que se pasó
        when(cultorRepository.save(any(Cultor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // --- 2. ACT (Actuar) ---
        ResponseEntity<CultorResponse> response = cultorService.update(cultorId, requestDto);

        // --- 3. ASSERT (Verificar) ---
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Actualizado", response.getBody().getLastName());
        assertEquals("Direccion Actualizada", response.getBody().getHomeAddress());
        assertEquals("0412-9999999", response.getBody().getPhoneNumber());

        // Verificamos que 'save' se llamó 1 vez
        verify(cultorRepository, times(1)).save(any(Cultor.class));
    }

    @SuppressWarnings("null")
    @Test
    public void whenUpdatingImmutableIdNumber_shouldThrowIllegalArgumentException() {
        // --- 1. ARRANGE (Preparar) ---
        Integer cultorId = 1;

        // 1.1 - Creamos el DTO (Request) con la Cédula cambiada
        CultorRequest requestDto = new CultorRequest(
            "Nombre", "Actualizado", "M", "V-666", // <-- Cédula Cambiada
            LocalDate.of(1990, 1, 1), "0412-9999999", null, null,
            1, 1, "Direccion", 1, 1, null, 11, null, null, null
        );

        // 1.2 - Creamos el cultor que "ya existe"
        Cultor cultorExistente = new Cultor("Nombre", "Viejo", "M", "V-12345", // <-- Cédula Original
            LocalDate.of(1990, 1, 1), "0412-1111111", null, null,
            null, null, "Direccion", null, null, null, 10, null, null, null);
        cultorExistente.setId(cultorId);

        // 1.3 - Configuración de Mockito (Solo necesitamos que encuentre al cultor)
        when(cultorRepository.findById(cultorId)).thenReturn(Optional.of(cultorExistente));

        // --- 2. ACT & 3. ASSERT ---
        // Verificamos que el servicio lanza la excepción ANTES de hacer cualquier otra cosa
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cultorService.update(cultorId, requestDto);
        });

        assertEquals("The IdNumber (Cédula) cannot be modified.", exception.getMessage());

        // Verificamos que NUNCA se llamó a 'save'
        verify(cultorRepository, never()).save(any(Cultor.class));
    }


    // ----------------------------------------------------------------
    // PRUEBAS PARA delete()
    // ----------------------------------------------------------------

    @Test
    public void whenDeleteIsValid_shouldReturn204NoContent() {
        // --- 1. ARRANGE (Preparar) ---
        Integer cultorId = 1;

        // Le decimos al repo: "Sí, el cultor con ID 1 existe"
        when(cultorRepository.existsById(cultorId)).thenReturn(true);
        // Configuramos el mock de deleteById para que no haga nada
        doNothing().when(cultorRepository).deleteById(cultorId);

        // --- 2. ACT (Actuar) ---
        ResponseEntity<Void> response = cultorService.delete(cultorId);

        // --- 3. ASSERT (Verificar) ---
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); // Esperamos 204

        // Verificamos que el método 'deleteById' fue llamado exactamente 1 vez con el ID correcto
        verify(cultorRepository, times(1)).deleteById(cultorId);
    }

    /**
     * Prueba el "camino triste": create() falla por teléfono duplicado.
     * Espera una DuplicateEntityException.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCreate_PhoneNumberIsDuplicated_shouldThrowDuplicateEntityException() {
        // --- 1. ARRANGE (Preparar) ---
        CultorRequest requestDto = new CultorRequest(
            "Nombre", "Cultor", "M", "V-123",
            LocalDate.of(1990, 1, 1), "0412-1111111", // <-- Teléfono duplicado
            "nuevo@mail.com", null, 1, 1, "Dir", 1, 1, null, 10, null, null, null
        );

        // 1.1 Configuración de Mockito (¡El Camino Triste!)
        // Le decimos al repo: "Cuando pregunten si el teléfono existe..."
        when(cultorRepository.existsByPhoneNumber("0412-1111111")).thenReturn(true);
        // "...responde que SÍ (true)."

        // --- 2. ACT & 3. ASSERT ---
        // Verificamos que el servicio detecta el duplicado y lanza la excepción
        assertThrows(DuplicateEntityException.class, () -> {
            cultorService.create(requestDto);
        });

        // Verificamos que NUNCA se intentó guardar nada
        verify(cultorRepository, never()).save(any(Cultor.class));
    }


    /**
     * Prueba el "camino triste": create() falla por una relación inválida.
     * (Ej. La Parroquia no pertenece al Municipio que se está referenciando).
     * Espera una IllegalArgumentException.
     */
    @SuppressWarnings("null")
    @Test
    public void whenCreate_ParishDoesNotBelongToMunicipality_shouldThrowIllegalArgument() {
        // --- 1. ARRANGE (Preparar) ---
        Integer municipalityId = 1;
        Integer parishId = 99; // Parroquia que no pertenece al Municipio 1
        
        CultorRequest requestDto = new CultorRequest(
            "Nombre", "Cultor", "M", "V-123", LocalDate.of(1990, 1, 1), 
            "0412-1111111", "test@mail.com", null, 
            municipalityId, parishId, // <-- IDs en conflicto
            "Dir", 1, 1, null, 10, null, null, null
        );
        
        // 1.1 Configuración de Mocks:
        // Cédula y teléfono pasan el filtro de duplicados (FALSE)
        when(cultorRepository.existsByIdNumber(anyString())).thenReturn(false);
        when(cultorRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(cultorRepository.existsByEmail(anyString())).thenReturn(false);
        
        // 1.2 Creamos los objetos para la Búsqueda (ArtDiscipline es dependiente de ArtCategory)
        Municipality m = new Municipality("Valencia"); m.setId(1);
        Municipality m2 = new Municipality("Otro"); m2.setId(2);
        Parish p = new Parish("San José", m2); p.setId(99); // <-- ¡La parroquia 99 pertenece al Municipio 2!

        // 1.3 Le decimos a Mockito que busque el Municipio 1 y la Parroquia 99
        when(municipalityRepository.findById(municipalityId)).thenReturn(Optional.of(m));
        when(parishRepository.findById(parishId)).thenReturn(Optional.of(p)); // Devuelve la parroquia "mala"
        when(artCategoryRepository.findById(anyInt())).thenReturn(Optional.of(new ArtCategory("Música")));
        when(artDisciplineRepository.findById(anyInt())).thenReturn(Optional.of(new ArtDiscipline("Guitarra", new ArtCategory("Música"))));

        // --- 2. ACT & 3. ASSERT ---
        // La prueba debe lanzar la excepción que definimos en validateParishId()
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cultorService.create(requestDto);
        });

        assertEquals("The Selected Parish Does Not Belong To The Chosen Municipality", exception.getMessage());
        verify(cultorRepository, never()).save(any(Cultor.class));
    }


    // ----------------------------------------------------------------
    // PRUEBAS PARA update() - CAMINOS TRISTES
    // ----------------------------------------------------------------

    /**
     * Prueba el "camino triste": update() falla por usar el teléfono de OTRO cultor.
     * Espera una DuplicateEntityException.
     */
    @SuppressWarnings("null")
    @Test
    public void whenUpdate_PhoneNumberIsDuplicatedByOtherCultor_shouldThrowDuplicateException() {
        // --- 1. ARRANGE (Preparar) ---
        Integer cultorId = 1;
        
        // 1.1 Creamos el DTO (Request) que intenta usar un teléfono prohibido
        CultorRequest requestDto = new CultorRequest(
            "Nombre", "Actualizado", "M", "V-12345", 
            LocalDate.of(1990, 1, 1), "0412-9999999", // <-- Teléfono duplicado
            "update@cultor.com", null,
            1, 1, "Dir", 1, 1, null, 11, null, null, null
        );

        // 1.2 Creamos el cultor que "ya existe" en la BD
        Cultor cultorExistente = new Cultor("Nombre", "Viejo", "M", "V-12345",
            LocalDate.of(1990, 1, 1), "0412-1111111", null, null,
            null, null, "Dir Vieja", null, null, null, 10, null, null, null);
        cultorExistente.setId(cultorId);

        // 1.3 Configuración de Mockito
        // Cuando busquen al cultor, devuélvelo
        when(cultorRepository.findById(cultorId)).thenReturn(Optional.of(cultorExistente));
        
        // Cuando se haga la verificación de duplicados para el teléfono...
        // ...respondemos que SÍ existe en OTRO lugar (true).
        when(cultorRepository.existsByPhoneNumberAndIdNot("0412-9999999", cultorId)).thenReturn(true);
        
        // --- 2. ACT & 3. ASSERT ---
        // Verificamos que lanza la excepción
        assertThrows(DuplicateEntityException.class, () -> {
            cultorService.update(cultorId, requestDto);
        });
        
        // Verificamos que 'save' NUNCA se llamó
        verify(cultorRepository, never()).save(any(Cultor.class));
    }
}