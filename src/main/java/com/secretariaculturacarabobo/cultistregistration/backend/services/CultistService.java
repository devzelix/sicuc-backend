package com.secretariaculturacarabobo.cultistregistration.backend.services;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.CultistRequest;
import com.secretariaculturacarabobo.cultistregistration.backend.dtos.CultistResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.ArtCategory;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.ArtDiscipline;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.Cultist;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.Municipality;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.Parish;
import com.secretariaculturacarabobo.cultistregistration.backend.exceptions.DuplicateEntityException;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.ArtCategoryRepository;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.ArtDisciplineRepository;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.CultistRepository;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.MunicipalityRepository;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.ParishRepository;
import com.secretariaculturacarabobo.cultistregistration.backend.specifications.CultistSpecification;
import com.secretariaculturacarabobo.cultistregistration.backend.utils.DateValidator;
import com.secretariaculturacarabobo.cultistregistration.backend.utils.StringUtils;
import com.secretariaculturacarabobo.cultistregistration.backend.utils.StringValidator;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service class for managing cultist-related operations.
 */
@Service
public class CultistService {

    private final CultistRepository cultistRepository;
    private final MunicipalityRepository municipalityRepository;
    private final ParishRepository parishRepository;
    private final ArtCategoryRepository artCategoryRepository;
    private final ArtDisciplineRepository artDisciplineRepository;

    /**
     * Constructor with dependency injection for all repositories used.
     */
    public CultistService(CultistRepository cultistRepository, MunicipalityRepository municipalityRepository,
            ParishRepository parishRepository, ArtCategoryRepository artCategoryRepository,
            ArtDisciplineRepository artDisciplineRepository) {
        this.cultistRepository = cultistRepository;
        this.municipalityRepository = municipalityRepository;
        this.parishRepository = parishRepository;
        this.artCategoryRepository = artCategoryRepository;
        this.artDisciplineRepository = artDisciplineRepository;
    }

    /**
     * Creates a new Cultist entity after validating uniqueness of key fields.
     * 
     * @param cultistRequest DTO containing data for the new cultist.
     * @return ResponseEntity with the created CultistResponse DTO.
     * @throws DuplicateEntityException if a cultist with the same ID, phone, email
     *                                  or Instagram username exists.
     * @throws IllegalArgumentException if data validation fails.
     */
    public ResponseEntity<CultistResponse> create(CultistRequest cultistRequest) {
        // Check if any unique fields already exist in the database
        if (cultistRepository.existsByIdNumber(cultistRequest.getIdNumber()))
            throw new DuplicateEntityException("Id Number Already Exists");
        if (cultistRepository.existsByPhoneNumber(cultistRequest.getPhoneNumber()))
            throw new DuplicateEntityException("Phone Number Already Exists");
        if (cultistRepository.existsByEmail(cultistRequest.getEmail()))
            throw new DuplicateEntityException("Email Already Exists");

        // Validate and normalize Instagram username if provided
        String instagramUser = cultistRequest.getInstagramUser();
        if (instagramUser != null && !instagramUser.trim().isEmpty()) {
            instagramUser = instagramUser.toLowerCase();
            if (cultistRepository.existsByInstagramUser(cultistRequest.getInstagramUser()))
                throw new DuplicateEntityException("Instagram Username Already Exists");
        }

        // Convert DTO to entity, validate fields, and save
        Cultist cultist = toCultist(cultistRequest);
        Cultist saved = cultistRepository.save(cultist);

        // Convert saved entity to response DTO and return
        return ResponseEntity.ok(toCultistResponse(saved));
    }

    /**
     * Retrieves a filtered list of CultistResponse objects based on given criteria.
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
     * @return List of CultistResponse DTOs matching filters.
     */
    public List<CultistResponse> getAllCultistsWithFilters(String query, String gender, Integer municipalityId,
            Integer parishId,
            Integer artCategoryId, Integer artDisciplineId,
            Boolean hasDisability, Boolean hasIllness) {
        Specification<Cultist> specification = CultistSpecification.withFilters(query,
                gender, municipalityId, parishId, artCategoryId, artDisciplineId, hasDisability,
                hasIllness);

        // Fetch and convert entities to response DTOs
        return cultistRepository.findAll(specification)
                .stream()
                .map(this::toCultistResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts CultistRequest DTO to Cultist entity after validating and
     * normalizing input data.
     * Also verifies existence and consistency of related entities.
     * 
     * @param cultistRequest Incoming DTO with cultist data.
     * @return Cultist entity ready for persistence.
     * @throws IllegalArgumentException If validation fails.
     * @throws EntityNotFoundException  If related entities are not found.
     */
    private Cultist toCultist(CultistRequest cultistRequest) {
        // Validate and normalize names
        String firstName = cultistRequest.getFirstName();
        if (!StringValidator.isValidName(firstName))
            throw new IllegalArgumentException("FirstName Is Invalid");
        firstName = StringUtils.toCapitalize(firstName);
        String lastName = cultistRequest.getLastName();
        if (!StringValidator.isValidName(lastName))
            throw new IllegalArgumentException("LastName Is Invalid");
        lastName = StringUtils.toCapitalize(lastName);

        // Validate gender field
        String gender = cultistRequest.getGender().trim().toUpperCase();
        if (!gender.equals("F") && !gender.equals("M"))
            throw new IllegalArgumentException("Gender Is Invalid");

        // Validate and trim other basic fields
        String idNumber = cultistRequest.getIdNumber().trim();
        LocalDate birthDate = cultistRequest.getBirthDate();
        if (!DateValidator.isValidBirthDate(birthDate))
            throw new IllegalArgumentException("BirthDate Is Invalid");
        String phoneNumber = cultistRequest.getPhoneNumber().trim();
        String email = cultistRequest.getEmail().trim().toLowerCase();

        // Normalize Instagram username or set null if blank
        String instagramUser = cultistRequest.getInstagramUser();
        if (instagramUser == null || instagramUser.isBlank()) {
            instagramUser = null;
        } else {
            instagramUser = instagramUser.trim().toLowerCase();
        }

        // Fetch and validate related entities
        Municipality municipality = municipalityRepository.findById(cultistRequest.getMunicipalityId())
                .orElseThrow(() -> new EntityNotFoundException("Municipality Not Found"));
        Parish parish = parishRepository.findById(cultistRequest.getParishId())
                .orElseThrow(() -> new EntityNotFoundException("Parish Not Found"));

        // Ensure parish belongs to municipality
        validateParishId(parish, cultistRequest);
        String homeAddress = cultistRequest.getHomeAddress();
        homeAddress = StringUtils.toCapitalize(homeAddress);
        ArtCategory artCategory = artCategoryRepository.findById(cultistRequest.getArtCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Art Category Not Found"));
        ArtDiscipline artDiscipline = artDisciplineRepository.findById(cultistRequest.getArtDisciplineId())
                .orElseThrow(() -> new EntityNotFoundException("Art Discipline Not Found"));

        // Ensure art discipline belongs to category
        validateArtDisciplineId(artDiscipline, cultistRequest);
        int yearsofExperience = cultistRequest.getYearsOfExperience();

        // Normalize group name or set null
        String groupName = cultistRequest.getGroupName();
        if (groupName == null || groupName.isBlank()) {
            groupName = null;
        } else {
            groupName = StringUtils.toCapitalize(groupName);
        }

        // Normalize disability and illness fields or set null
        String disability = cultistRequest.getDisability();
        if (disability == null || disability.isBlank()) {
            disability = null;
        } else {
            disability = disability.trim().toLowerCase();
        }
        String illness = cultistRequest.getIllness();
        if (illness == null || illness.isBlank()) {
            illness = null;
        } else {
            illness = illness.trim().toLowerCase();
        }

        // Create and populate Cultist entity
        Cultist cultist = new Cultist();
        cultist.setFirstName(firstName);
        cultist.setLastName(lastName);
        cultist.setGender(gender);
        cultist.setIdNumber(idNumber);
        cultist.setBirthDate(birthDate);
        cultist.setPhoneNumber(phoneNumber);
        cultist.setEmail(email);
        cultist.setInstagramUser(instagramUser);
        cultist.setMunicipality(municipality);
        cultist.setParish(parish);
        cultist.setHomeAddress(homeAddress);
        cultist.setArtCategory(artCategory);
        cultist.setArtDiscipline(artDiscipline);
        cultist.setYearsOfExperience(yearsofExperience);
        cultist.setGroupName(groupName);
        cultist.setDisability(disability);
        cultist.setIllness(illness);

        return cultist;
    }

    /**
     * Converts Cultist entity to CultistResponse DTO for client consumption.
     * 
     * @param cultist Entity to convert.
     * @return DTO representing the cultist.
     */
    private CultistResponse toCultistResponse(Cultist cultist) {
        int id = cultist.getId();
        String firstName = cultist.getFirstName();
        String lastName = cultist.getLastName();
        String gender = cultist.getGender();
        String idNumber = cultist.getIdNumber();
        LocalDate birthDate = cultist.getBirthDate();
        String phoneNumber = cultist.getPhoneNumber();
        String email = cultist.getEmail();
        String instagramUser = cultist.getInstagramUser();
        int municipalityId = cultist.getMunicipality().getId();
        int parishId = cultist.getParish().getId();
        String homeAddress = cultist.getHomeAddress();
        int artCategoryId = cultist.getArtCategory().getId();
        int artDisciplineId = cultist.getArtDiscipline().getId();
        int yearsOfExperience = cultist.getYearsOfExperience();
        String groupName = cultist.getGroupName();
        String disability = cultist.getDisability();
        String illness = cultist.getIllness();

        CultistResponse cultistResponse = new CultistResponse(id, firstName, lastName, gender, idNumber,
                birthDate,
                phoneNumber,
                email,
                instagramUser, municipalityId, parishId, homeAddress, artCategoryId, artDisciplineId,
                yearsOfExperience,
                groupName,
                disability, illness);

        return cultistResponse;
    }

    /**
     * Validates that the selected Parish belongs to the specified Municipality.
     * 
     * @param parish         Parish entity to check.
     * @param cultistRequest Request containing the Municipality ID.
     * @throws IllegalArgumentException if Parish does not belong to Municipality.
     */
    private void validateParishId(Parish parish, CultistRequest cultistRequest) {
        if (parish.getMunicipality().getId() != cultistRequest.getMunicipalityId()) {
            throw new IllegalArgumentException(
                    "The Selected Parish Does Not Belong To The Chosen Municipality");
        }
    }

    /**
     * Validates that the selected Art Discipline belongs to the specified Art
     * Category.
     * 
     * @param artDiscipline  ArtDiscipline entity to check.
     * @param cultistRequest Request containing the Art Category ID.
     * @throws IllegalArgumentException if Discipline does not belong to Category.
     */
    private void validateArtDisciplineId(ArtDiscipline artDiscipline, CultistRequest cultistRequest) {
        if (artDiscipline.getArtCategory().getId() != cultistRequest.getArtCategoryId()) {
            throw new IllegalArgumentException(
                    "The Selected Discipline Does Not Belong To The Chosen Category");
        }
    }

}
