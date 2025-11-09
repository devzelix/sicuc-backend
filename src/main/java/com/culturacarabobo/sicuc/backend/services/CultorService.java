package com.culturacarabobo.sicuc.backend.services;

import jakarta.persistence.EntityNotFoundException;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * Service layer responsible for all business logic related to {@link Cultor} entities.
 * This includes creation, validation, updates, deletion, and querying.
 */
@Service
public class CultorService {

    // Repositories are declared 'final' and injected via the constructor
    private final CultorRepository cultorRepository;
    private final MunicipalityRepository municipalityRepository;
    private final ParishRepository parishRepository;
    private final ArtCategoryRepository artCategoryRepository;
    private final ArtDisciplineRepository artDisciplineRepository;

    /**
     * Constructs the service and injects all required repositories.
     * Spring automatically provides these dependencies.
     *
     * @param cultorRepository        Repository for {@link Cultor} data access.
     * @param municipalityRepository  Repository for {@link Municipality} data access.
     * @param parishRepository        Repository for {@link Parish} data access.
     * @param artCategoryRepository   Repository for {@link ArtCategory} data access.
     * @param artDisciplineRepository Repository for {@link ArtDiscipline} data access.
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
     * Creates a new Cultor after performing extensive validation.
     *
     * @param cultorRequest The DTO containing the data for the new cultor.
     * @return A {@link ResponseEntity} with the created {@link CultorResponse} and HTTP 201 (Created).
     * @throws DuplicateEntityException If idNumber, phoneNumber, email, or instagramUser already exist.
     * @throws EntityNotFoundException  If related entities (Municipality, Parish, etc.) are not found.
     * @throws IllegalArgumentException If business rules (e.g., validations, relationships) fail.
     */
    @SuppressWarnings("null")
    public ResponseEntity<CultorResponse> create(CultorRequest cultorRequest) {
        // 1. Perform preliminary uniqueness checks
        if (cultorRepository.existsByIdNumber(cultorRequest.getIdNumber()))
            throw new DuplicateEntityException("Id Number Already Exists");
        if (cultorRepository.existsByPhoneNumber(cultorRequest.getPhoneNumber()))
            throw new DuplicateEntityException("Phone Number Already Exists");

        // 2. Uniqueness check for optional email
        String email = cultorRequest.getEmail();
        if (email != null && !email.trim().isEmpty()) {
            email = email.toLowerCase();
            if (cultorRepository.existsByEmail(email)) // Use normalized email
                throw new DuplicateEntityException("Email Already Exists");
        }

        // 3. Uniqueness check for optional instagramUser
        String instagramUser = cultorRequest.getInstagramUser();
        if (instagramUser != null && !instagramUser.trim().isEmpty()) {
            instagramUser = instagramUser.toLowerCase();
            if (cultorRepository.existsByInstagramUser(instagramUser)) // Use normalized user
                throw new DuplicateEntityException("Instagram Username Already Exists");
        }

        // 4. Map DTO to Entity, performing all deep validations
        Cultor cultor = mapAndValidateCultor(new Cultor(), cultorRequest);
        
        // 5. Save the new entity
        Cultor saved = cultorRepository.save(cultor);

        // 6. Build the 201 Created response
        URI location = URI.create("/cultors/" + saved.getId());
        return ResponseEntity.created(location).body(toCultorResponse(saved));
    }

    /**
     * Updates an existing Cultor by its ID.
     * <p>
     * This method performs critical business rule validations:
     * 1. Checks that the Cultor exists.
     * 2. Verifies that immutable fields (idNumber, birthDate) have not been changed.
     * 3. Checks for uniqueness conflicts (phoneNumber, email) against *other* cultores.
     *
     * @param id            The ID of the cultor to update.
     * @param cultorRequest The DTO containing the new data.
     * @return A {@link ResponseEntity} with the updated {@link CultorResponse} and HTTP 200 (OK).
     * @throws EntityNotFoundException  If the cultor with the given ID is not found.
     * @throws DuplicateEntityException If unique fields conflict with another existing cultor.
     * @throws IllegalArgumentException If an immutable field is changed or business rules fail.
     */
    public ResponseEntity<CultorResponse> update(Integer id, CultorRequest cultorRequest) {
        // 1. Find the existing entity or throw 404
        Cultor cultorExisting = cultorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cultor Not Found With Id: " + id));

        // 2. Enforce business rule: Immutable fields
        if (!cultorExisting.getIdNumber().equals(cultorRequest.getIdNumber())) {
            throw new IllegalArgumentException("The IdNumber (Cédula) cannot be modified.");
        }
        if (!cultorExisting.getBirthDate().equals(cultorRequest.getBirthDate())) {
            throw new IllegalArgumentException("The Birth Date cannot be modified.");
        }

        // 3. Enforce business rule: Uniqueness checks for MUTABLE fields
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

        // 4. Map DTO data onto the existing entity, validating all fields
        mapAndValidateCultor(cultorExisting, cultorRequest);

        // 5. Save the updated entity
        Cultor saved = cultorRepository.save(cultorExisting);

        // 6. Return 200 OK
        return ResponseEntity.ok(toCultorResponse(saved));
    }

    /**
     * Retrieves a paginated list of cultors based on dynamic filter criteria.
     * Uses {@link CultorSpecification} to build the query.
     *
     * @param query           (and all other params)...
     * @param pageable        The pagination information (page, size, sort).
     * @return A {@link Page} of {@link CultorResponse} DTOs.
     */
    public Page<CultorResponse> getAllCultorsWithFilters(String query, String gender, Integer municipalityId,
            Integer parishId,
            Integer artCategoryId, Integer artDisciplineId,
            Boolean hasDisability, Boolean hasIllness,
            Pageable pageable) {
        
        // 1. Build the dynamic query
        Specification<Cultor> specification = CultorSpecification.withFilters(query,
                gender, municipalityId, parishId, artCategoryId, artDisciplineId, hasDisability,
                hasIllness);

        // 2. Execute the paginated find
        Page<Cultor> cultorPage = cultorRepository.findAll(specification, pageable);

        // 3. Convert the Page<Entity> to Page<DTO>
        return cultorPage.map(this::toCultorResponse);
    }

    /**
     * Retrieves a single cultor by its primary key ID.
     *
     * @param id The ID of the cultor.
     * @return A {@link ResponseEntity} with the found {@link CultorResponse} and HTTP 200 (OK).
     * @throws EntityNotFoundException If the ID is not found.
     */
    @SuppressWarnings("null")
    public ResponseEntity<CultorResponse> getById(Integer id) {
        // Find by ID or throw 404
        Cultor cultor = cultorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cultor Not Found With Id: " + id));

        return ResponseEntity.ok(toCultorResponse(cultor));
    }

    /**
     * Deletes a cultor by its ID.
     *
     * @param id The ID of the cultor to delete.
     * @return A {@link ResponseEntity} with HTTP 204 (No Content).
     * @throws EntityNotFoundException If the ID is not found.
     */
    public ResponseEntity<Void> delete(Integer id) {
        // 1. Check if it exists first (to provide a clear 404)
        if (!cultorRepository.existsById(id)) {
            throw new EntityNotFoundException("Cultor Not Found With Id: " + id);
        }

        // 2. Delete the entity
        cultorRepository.deleteById(id);

        // 3. Return 204 No Content
        return ResponseEntity.noContent().build();
    }

    // ----------------------------------------------------------------
    // PRIVATE HELPER METHODS
    // ----------------------------------------------------------------

    /**
     * Core private helper. Maps data from a {@link CultorRequest} DTO to a {@link Cultor} entity.
     * <p>
     * This method performs all field-level validation, normalization (trim, toLowerCase),
     * and relational integrity checks (e.g., Parish belongs to Municipality).
     * <p>
     * It's designed to be reusable for both {@code create} (with a new Cultor()) and
     * {@code update} (with an existing Cultor).
     *
     * @param cultor        The entity to map data onto (can be new or existing).
     * @param cultorRequest The DTO source of new data.
     * @return The same {@link Cultor} entity, now populated and validated.
     * @throws EntityNotFoundException  If related entities (Municipality, etc.) are not found.
     * @throws IllegalArgumentException If any business rule or validation fails.
     */
    @SuppressWarnings("null")
    private Cultor mapAndValidateCultor(Cultor cultor, CultorRequest cultorRequest) {

        // --- Basic Field Validation & Normalization ---
        String firstName = cultorRequest.getFirstName();
        if (!StringValidator.isValidName(firstName))
            throw new IllegalArgumentException("FirstName Is Invalid");
        firstName = StringUtils.toCapitalize(firstName);

        String lastName = cultorRequest.getLastName();
        if (!StringValidator.isValidName(lastName))
            throw new IllegalArgumentException("LastName Is Invalid");
        lastName = StringUtils.toCapitalize(lastName);

        String gender = cultorRequest.getGender().trim().toUpperCase();
        if (!gender.equals("F") && !gender.equals("M"))
            throw new IllegalArgumentException("Gender Is Invalid");

        String idNumber = cultorRequest.getIdNumber().trim();

        LocalDate birthDate = cultorRequest.getBirthDate();
        if (!DateValidator.isValidBirthDate(birthDate))
            throw new IllegalArgumentException("BirthDate Is Invalid");

        String phoneNumber = cultorRequest.getPhoneNumber().trim();

        // --- Nullable Field Normalization (Email, Instagram) ---
        String email = cultorRequest.getEmail();
        email = (email == null || email.isBlank()) ? null : email.trim().toLowerCase();

        String instagramUser = cultorRequest.getInstagramUser();
        instagramUser = (instagramUser == null || instagramUser.isBlank()) ? null : instagramUser.trim().toLowerCase();

        // --- Relational Integrity Checks (Location) ---
        Municipality municipality = municipalityRepository.findById(cultorRequest.getMunicipalityId())
                .orElseThrow(() -> new EntityNotFoundException("Municipality Not Found"));
        Parish parish = parishRepository.findById(cultorRequest.getParishId())
                .orElseThrow(() -> new EntityNotFoundException("Parish Not Found"));

        validateParishId(parish, cultorRequest); // Business rule check
        String homeAddress = StringUtils.toCapitalize(cultorRequest.getHomeAddress());

        // --- Relational Integrity Checks (Arts) ---
        ArtCategory artCategory = artCategoryRepository.findById(cultorRequest.getArtCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Art Category Not Found"));
        ArtDiscipline artDiscipline = artDisciplineRepository.findById(cultorRequest.getArtDisciplineId())
                .orElseThrow(() -> new EntityNotFoundException("Art Discipline Not Found"));
        
        String otherDiscipline = cultorRequest.getOtherDiscipline();
        otherDiscipline = (otherDiscipline == null || otherDiscipline.isBlank()) ? null : StringUtils.toCapitalize(otherDiscipline.trim());

        validateArtDisciplineId(artDiscipline, cultorRequest); // Business rule check
        validateOtherDiscipline(artDiscipline, otherDiscipline); // Business rule check

        // --- Other Fields ---
        int yearsofExperience = cultorRequest.getYearsOfExperience();

        String groupName = cultorRequest.getGroupName();
        groupName = (groupName == null || groupName.isBlank()) ? null : StringUtils.toCapitalize(groupName);

        String disability = cultorRequest.getDisability();
        disability = (disability == null || disability.isBlank()) ? null : disability.trim().toLowerCase();

        String illness = cultorRequest.getIllness();
        illness = (illness == null || illness.isBlank()) ? null : illness.trim().toLowerCase();

        // --- Set all fields on the entity ---
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
     * Converts a {@link Cultor} entity to its public-facing {@link CultorResponse} DTO.
     *
     * @param cultor The entity to convert.
     * @return The corresponding DTO.
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

        return new CultorResponse(id, firstName, lastName, gender, idNumber,
                birthDate, phoneNumber, email, instagramUser, municipalityId, parishId, homeAddress, artCategoryId,
                artDisciplineId, otherDiscipline, yearsOfExperience, groupName, disability, illness, createdAt);
    }

    /**
     * Validates that the selected Parish belongs to the specified Municipality.
     *
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
     * @throws IllegalArgumentException if Discipline does not belong to Category.
     */
    private void validateArtDisciplineId(ArtDiscipline artDiscipline, CultorRequest cultorRequest) {
        if (artDiscipline.getArtCategory().getId() != cultorRequest.getArtCategoryId()) {
            throw new IllegalArgumentException(
                    "The Selected Discipline Does Not Belong To The Chosen Category");
        }
    }

    /**
     * Validates the logic for the 'otherDiscipline' field.
     * Ensures 'otherDiscipline' is ONLY provided if the selected discipline is "Otra...",
     * and MUST be provided if it is.
     *
     * @throws IllegalArgumentException If the logic is violated.
     */
    private void validateOtherDiscipline(ArtDiscipline artDiscipline, String otherDiscipline) {
        if (!artDiscipline.getName().equals("Otra...") && otherDiscipline != null && !otherDiscipline.isBlank()) {
            throw new IllegalArgumentException("The Selected Discipline Does Not Is \"Otra...\"");
        } else if (artDiscipline.getName().equals("Otra...")
                && (otherDiscipline == null || otherDiscipline.isBlank())) {
            throw new IllegalArgumentException("OtherDiscipline Is Requerid");
        }
    }
}