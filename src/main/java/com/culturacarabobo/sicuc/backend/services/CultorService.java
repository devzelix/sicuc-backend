package com.culturacarabobo.sicuc.backend.services;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
import com.culturacarabobo.sicuc.backend.specifications.CultorSpecification;
import com.culturacarabobo.sicuc.backend.utils.DateValidator;
import com.culturacarabobo.sicuc.backend.utils.StringUtils;
import com.culturacarabobo.sicuc.backend.utils.StringValidator;

/**
 * Service class for managing cultor-related operations.
 */
@Service
public class CultorService {

    private final CultorRepository cultorRepository;
    private final MunicipalityRepository municipalityRepository;
    private final ParishRepository parishRepository;
    private final ArtCategoryRepository artCategoryRepository;
    private final ArtDisciplineRepository artDisciplineRepository;

    /**
     * Constructor with dependency injection for all repositories used.
     */
    public CultorService(CultorRepository cultorRepository, MunicipalityRepository municipalityRepository,
            ParishRepository parishRepository, ArtCategoryRepository artCategoryRepository,
            ArtDisciplineRepository artDisciplineRepository) {
        this.cultorRepository = cultorRepository;
        this.municipalityRepository = municipalityRepository;
        this.parishRepository = parishRepository;
        this.artCategoryRepository = artCategoryRepository;
        this.artDisciplineRepository = artDisciplineRepository;
    }

    /**
     * Creates a new Cultor entity after validating uniqueness of key fields.
     * 
     * @param cultorRequest DTO containing data for the new cultor.
     * @return ResponseEntity with the created CultorResponse DTO.
     * @throws DuplicateEntityException if a cultor with the same ID, phone, email
     *                                  or Instagram username exists.
     * @throws IllegalArgumentException if data validation fails.
     */
    public ResponseEntity<CultorResponse> create(CultorRequest cultorRequest) {
        // Check if any unique fields already exist in the database
        if (cultorRepository.existsByIdNumber(cultorRequest.getIdNumber()))
            throw new DuplicateEntityException("Id Number Already Exists");
        if (cultorRepository.existsByPhoneNumber(cultorRequest.getPhoneNumber()))
            throw new DuplicateEntityException("Phone Number Already Exists");

        // Validate and normalize Email if provided
        String email = cultorRequest.getEmail();
        if (email != null && !email.trim().isEmpty()) {
            email = email.toLowerCase();
            if (cultorRepository.existsByEmail(cultorRequest.getEmail()))
                throw new DuplicateEntityException("Email Already Exists");
        }

        // Validate and normalize Instagram username if provided
        String instagramUser = cultorRequest.getInstagramUser();
        if (instagramUser != null && !instagramUser.trim().isEmpty()) {
            instagramUser = instagramUser.toLowerCase();
            if (cultorRepository.existsByInstagramUser(cultorRequest.getInstagramUser()))
                throw new DuplicateEntityException("Instagram Username Already Exists");
        }

        // Convert DTO to entity, validate fields, and save
        Cultor cultor = mapAndValidateCultor(new Cultor(), cultorRequest);
        Cultor saved = cultorRepository.save(cultor);

        // Convert saved entity to response DTO and return
        return ResponseEntity.ok(toCultorResponse(saved));
    }

    /**
     * NUEVO MÉTODO: Actualiza un Cultor existente.
     *
     * @param id            ID del cultor a actualizar.
     * @param cultorRequest DTO con los nuevos datos.
     * @return ResponseEntity con el CultorResponse actualizado.
     * @throws EntityNotFoundException  si el cultor no se encuentra.
     * @throws DuplicateEntityException si los nuevos datos únicos ya existen en otro cultor.
     * @throws IllegalArgumentException si la validación de datos falla.
     */
    public ResponseEntity<CultorResponse> update(Integer id, CultorRequest cultorRequest) {
        // 1. Buscar el cultor existente
        Cultor cultorExisting = cultorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cultor Not Found With Id: " + id));

        // 2. VALIDACIÓN DE INMUTABILIDAD (¡NUEVO!)
        // Verificamos que la cédula en el request sea la misma que la guardada.
        if (!cultorExisting.getIdNumber().equals(cultorRequest.getIdNumber())) {
            throw new IllegalArgumentException("The IdNumber (Cédula) cannot be modified.");
        }
        
        // Verificamos que la fecha de nacimiento sea la misma.
        if (!cultorExisting.getBirthDate().equals(cultorRequest.getBirthDate())) {
             throw new IllegalArgumentException("The Birth Date cannot be modified.");
        }

        // 3. Validar campos únicos (excluyendo al cultor actual)
        if (cultorRepository.existsByPhoneNumberAndIdNot(cultorRequest.getPhoneNumber(), id))
            throw new DuplicateEntityException("Phone Number Already Exists");

        String email = cultorRequest.getEmail();
        if (email != null && !email.trim().isEmpty()) {
            email = email.toLowerCase();
            if (cultorRepository.existsByEmailAndIdNot(email, id))
                throw new DuplicateEntityException("Email Already Exists");
        }

        String instagramUser = cultorRequest.getInstagramUser();
        if (instagramUser != null && !instagramUser.trim().isEmpty()) {
            instagramUser = instagramUser.toLowerCase();
            if (cultorRepository.existsByInstagramUserAndIdNot(instagramUser, id))
                throw new DuplicateEntityException("Instagram Username Already Exists");
        }

        // 3. Mapear y validar los datos del DTO a la entidad existente
        // Usamos el método refactorizado
        mapAndValidateCultor(cultorExisting, cultorRequest);

        // 4. Guardar los cambios
        Cultor saved = cultorRepository.save(cultorExisting);

        // 5. Devolver la respuesta
        return ResponseEntity.ok(toCultorResponse(saved));
    }

    /**
     * Retrieves a filtered list of CultorResponse objects based on given criteria.
     * Uses specifications for dynamic query building.
     * 
     * @param query           Search keyword for names or other text fields.
     * @param gender          Filter by gender ('F' or 'M').
     * @param municipalityId  Filter by municipality.
     * @param parishId        Filter by parish.
     * @param artCategoryId   Filter by art category.
     * @param artDisciplineId Filter by art discipline.
     * @param hasDisability   Filter by disability presence.
     * @param hasIllness      Filter by illness presence.
     * @return List of CultorResponse DTOs matching filters.
     */
    @SuppressWarnings("null")
    public List<CultorResponse> getAllCultorsWithFilters(String query, String gender, Integer municipalityId,
            Integer parishId,
            Integer artCategoryId, Integer artDisciplineId,
            Boolean hasDisability, Boolean hasIllness) {
        Specification<Cultor> specification = CultorSpecification.withFilters(query,
                gender, municipalityId, parishId, artCategoryId, artDisciplineId, hasDisability,
                hasIllness);

        // Fetch and convert entities to response DTOs
        return cultorRepository.findAll(specification)
                .stream()
                .map(this::toCultorResponse)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO 'toCultor' REFACTORIZADO Y RENOMBRADO
     * Ahora se llama 'mapAndValidateCultor' y actualiza un objeto 'Cultor' existente
     * o uno nuevo.
     *
     * @param cultor        La entidad Cultor a rellenar (puede ser nueva o existente).
     * @param cultorRequest Incoming DTO con cultor data.
     * @return El objeto Cultor (pasado como argumento) rellenado y validado.
     */
    @SuppressWarnings("null")
    private Cultor mapAndValidateCultor(Cultor cultor, CultorRequest cultorRequest) {

        // Validate and normalize names
        String firstName = cultorRequest.getFirstName();
        if (!StringValidator.isValidName(firstName))
            throw new IllegalArgumentException("FirstName Is Invalid");
        firstName = StringUtils.toCapitalize(firstName);
        String lastName = cultorRequest.getLastName();
        if (!StringValidator.isValidName(lastName))
            throw new IllegalArgumentException("LastName Is Invalid");
        lastName = StringUtils.toCapitalize(lastName);
        // Validate gender field
        String gender = cultorRequest.getGender().trim().toUpperCase();
        if (!gender.equals("F") && !gender.equals("M"))
            throw new IllegalArgumentException("Gender Is Invalid");

        // Validate and trim other basic fields
        String idNumber = cultorRequest.getIdNumber().trim();

        LocalDate birthDate = cultorRequest.getBirthDate();
        if (!DateValidator.isValidBirthDate(birthDate))
            throw new IllegalArgumentException("BirthDate Is Invalid");

        String phoneNumber = cultorRequest.getPhoneNumber().trim();

        // Normalize Email or set null if blank
        String email = cultorRequest.getEmail();
        if (email == null || email.isBlank()) {
            email = null;
        } else {
            email = email.trim().toLowerCase();
        }

        // Normalize Instagram username or set null if blank
        String instagramUser = cultorRequest.getInstagramUser();
        if (instagramUser == null || instagramUser.isBlank()) {
            instagramUser = null;
        } else {
            instagramUser = instagramUser.trim().toLowerCase();
        }

        // Fetch and validate related entities
        Municipality municipality = municipalityRepository.findById(cultorRequest.getMunicipalityId())
                .orElseThrow(() -> new EntityNotFoundException("Municipality Not Found"));
        Parish parish = parishRepository.findById(cultorRequest.getParishId())
                .orElseThrow(() -> new EntityNotFoundException("Parish Not Found"));

        // Ensure parish belongs to municipality
        validateParishId(parish, cultorRequest);
        String homeAddress = cultorRequest.getHomeAddress();
        homeAddress = StringUtils.toCapitalize(homeAddress);
        ArtCategory artCategory = artCategoryRepository.findById(cultorRequest.getArtCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Art Category Not Found"));
        ArtDiscipline artDiscipline = artDisciplineRepository.findById(cultorRequest.getArtDisciplineId())
                .orElseThrow(() -> new EntityNotFoundException("Art Discipline Not Found"));
        String otherDiscipline = cultorRequest.getOtherDiscipline();
        if (otherDiscipline == null || otherDiscipline.isBlank()) {
            otherDiscipline = null;
        } else {
            otherDiscipline = StringUtils.toCapitalize(otherDiscipline.trim());
        }
        // Ensure art discipline belongs to category
        validateArtDisciplineId(artDiscipline, cultorRequest);
        validateOtherDiscipline(artDiscipline, otherDiscipline);

        int yearsofExperience = cultorRequest.getYearsOfExperience();

        // Normalize group name or set null
        String groupName = cultorRequest.getGroupName();
        if (groupName == null || groupName.isBlank()) {
            groupName = null;
        } else {
            groupName = StringUtils.toCapitalize(groupName);
        }

        // Normalize disability and illness fields or set null
        String disability = cultorRequest.getDisability();
        if (disability == null || disability.isBlank()) {
            disability = null;
        } else {
            disability = disability.trim().toLowerCase();
        }
        String illness = cultorRequest.getIllness();
        if (illness == null || illness.isBlank()) {
            illness = null;
        } else {
            illness = illness.trim().toLowerCase();
        }

        // Create and populate Cultor entity
        cultor.setFirstName(firstName);
        cultor.setLastName(lastName);
        cultor.setGender(gender);
        cultor.setIdNumber(idNumber);
        cultor.setBirthDate(birthDate);
        cultor.setPhoneNumber(phoneNumber);
        cultor.setEmail(email);
        cultor.setInstagramUser(instagramUser);
        cultor.setMunicipality(municipality);
        cultor.setParish(parish);
        cultor.setHomeAddress(homeAddress);
        cultor.setArtCategory(artCategory);
        cultor.setArtDiscipline(artDiscipline);
        cultor.setOtherDiscipline(otherDiscipline);
        cultor.setYearsOfExperience(yearsofExperience);
        cultor.setGroupName(groupName);
        cultor.setDisability(disability);
        cultor.setIllness(illness);

        return cultor;
    }

    /**
     * Converts Cultor entity to CultorResponse DTO for client consumption.
     * 
     * @param cultor Entity to convert.
     * @return DTO representing the cultor.
     */
    private CultorResponse toCultorResponse(Cultor cultor) {
        int id = cultor.getId();
        String firstName = cultor.getFirstName();
        String lastName = cultor.getLastName();
        String gender = cultor.getGender();
        String idNumber = cultor.getIdNumber();
        LocalDate birthDate = cultor.getBirthDate();
        String phoneNumber = cultor.getPhoneNumber();
        String email = cultor.getEmail();
        String instagramUser = cultor.getInstagramUser();
        int municipalityId = cultor.getMunicipality().getId();
        int parishId = cultor.getParish().getId();
        String homeAddress = cultor.getHomeAddress();
        int artCategoryId = cultor.getArtCategory().getId();
        int artDisciplineId = cultor.getArtDiscipline().getId();
        String otherDiscipline = cultor.getOtherDiscipline();
        int yearsOfExperience = cultor.getYearsOfExperience();
        String groupName = cultor.getGroupName();
        String disability = cultor.getDisability();
        String illness = cultor.getIllness();
        LocalDate createdAt = cultor.getCreatedAt();

        CultorResponse cultorResponse = new CultorResponse(id, firstName, lastName, gender, idNumber,
                birthDate, phoneNumber, email, instagramUser, municipalityId, parishId, homeAddress, artCategoryId,
                artDisciplineId, otherDiscipline, yearsOfExperience, groupName, disability, illness, createdAt);

        return cultorResponse;
    }

    /**
     * Validates that the selected Parish belongs to the specified Municipality.
     * 
     * @param parish        Parish entity to check.
     * @param cultorRequest Request containing the Municipality ID.
     * @throws IllegalArgumentException if Parish does not belong to Municipality.
     */
    private void validateParishId(Parish parish, CultorRequest cultorRequest) {
        if (parish.getMunicipality().getId() != cultorRequest.getMunicipalityId()) {
            throw new IllegalArgumentException(
                    "The Selected Parish Does Not Belong To The Chosen Municipality");
        }
    }

    /**
     * Validates that the selected Art Discipline belongs to the specified Art
     * Category.
     * 
     * @param artDiscipline ArtDiscipline entity to check.
     * @param cultorRequest Request containing the Art Category ID.
     * @throws IllegalArgumentException if Discipline does not belong to Category.
     */
    private void validateArtDisciplineId(ArtDiscipline artDiscipline, CultorRequest cultorRequest) {
        if (artDiscipline.getArtCategory().getId() != cultorRequest.getArtCategoryId()) {
            throw new IllegalArgumentException(
                    "The Selected Discipline Does Not Belong To The Chosen Category");
        }
    }

    private void validateOtherDiscipline(ArtDiscipline artDiscipline, String otherDiscipline) {
        if (!artDiscipline.getName().equals("Otra...") && otherDiscipline != null && !otherDiscipline.isBlank()) {
            throw new IllegalArgumentException("The Selected Discipline Does Not Is \"Otra...\"");
        } else if (artDiscipline.getName().equals("Otra...")
                && (otherDiscipline == null || otherDiscipline.isBlank())) {
            throw new IllegalArgumentException("OtherDiscipline Is Requerid");
        }
    }

    /**
     * Elimina un cultor por su ID.
     *
     * @param id El ID del cultor a eliminar.
     * @return ResponseEntity sin contenido (204 No Content).
     * @throws EntityNotFoundException si el cultor no se encuentra.
     */
    public ResponseEntity<Void> delete(Integer id) {
        // 1. Verificar si el cultor existe antes de intentar borrar
        if (!cultorRepository.existsById(id)) {
            throw new EntityNotFoundException("Cultor Not Found With Id: " + id);
        }

        // 2. Si existe, eliminarlo
        cultorRepository.deleteById(id);

        // 3. Devolver una respuesta HTTP 204 No Content (éxito, sin cuerpo)
        return ResponseEntity.noContent().build();
    }

}
