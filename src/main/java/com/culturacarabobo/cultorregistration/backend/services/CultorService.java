package com.culturacarabobo.cultorregistration.backend.services;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.culturacarabobo.cultorregistration.backend.dtos.CultorRequest;
import com.culturacarabobo.cultorregistration.backend.dtos.CultorResponse;
import com.culturacarabobo.cultorregistration.backend.entities.ArtCategory;
import com.culturacarabobo.cultorregistration.backend.entities.ArtDiscipline;
import com.culturacarabobo.cultorregistration.backend.entities.Cultor;
import com.culturacarabobo.cultorregistration.backend.entities.Municipality;
import com.culturacarabobo.cultorregistration.backend.entities.Parish;
import com.culturacarabobo.cultorregistration.backend.exceptions.DuplicateEntityException;
import com.culturacarabobo.cultorregistration.backend.repositories.ArtCategoryRepository;
import com.culturacarabobo.cultorregistration.backend.repositories.ArtDisciplineRepository;
import com.culturacarabobo.cultorregistration.backend.repositories.CultorRepository;
import com.culturacarabobo.cultorregistration.backend.repositories.MunicipalityRepository;
import com.culturacarabobo.cultorregistration.backend.repositories.ParishRepository;
import com.culturacarabobo.cultorregistration.backend.specifications.CultorSpecification;
import com.culturacarabobo.cultorregistration.backend.utils.DateValidator;
import com.culturacarabobo.cultorregistration.backend.utils.StringUtils;
import com.culturacarabobo.cultorregistration.backend.utils.StringValidator;

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
        if (cultorRepository.existsByEmail(cultorRequest.getEmail()))
            throw new DuplicateEntityException("Email Already Exists");

        // Validate and normalize Instagram username if provided
        String instagramUser = cultorRequest.getInstagramUser();
        if (instagramUser != null && !instagramUser.trim().isEmpty()) {
            instagramUser = instagramUser.toLowerCase();
            if (cultorRepository.existsByInstagramUser(cultorRequest.getInstagramUser()))
                throw new DuplicateEntityException("Instagram Username Already Exists");
        }

        // Convert DTO to entity, validate fields, and save
        Cultor cultor = toCultor(cultorRequest);
        Cultor saved = cultorRepository.save(cultor);

        // Convert saved entity to response DTO and return
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
     * Converts CultorRequest DTO to Cultor entity after validating and
     * normalizing input data.
     * Also verifies existence and consistency of related entities.
     * 
     * @param cultorRequest Incoming DTO with cultor data.
     * @return Cultor entity ready for persistence.
     * @throws IllegalArgumentException If validation fails.
     * @throws EntityNotFoundException  If related entities are not found.
     */
    private Cultor toCultor(CultorRequest cultorRequest) {
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
        String email = cultorRequest.getEmail().trim().toLowerCase();

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

        // Ensure art discipline belongs to category
        validateArtDisciplineId(artDiscipline, cultorRequest);
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
        Cultor cultor = new Cultor();
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
        int yearsOfExperience = cultor.getYearsOfExperience();
        String groupName = cultor.getGroupName();
        String disability = cultor.getDisability();
        String illness = cultor.getIllness();
        LocalDate createdAt = cultor.getCreatedAt();

        CultorResponse cultorResponse = new CultorResponse(id, firstName, lastName, gender, idNumber,
                birthDate,
                phoneNumber,
                email,
                instagramUser, municipalityId, parishId, homeAddress, artCategoryId, artDisciplineId,
                yearsOfExperience,
                groupName,
                disability, illness, createdAt);

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

}
