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

@Service
public class CultistService {

        private final CultistRepository cultistRepository;
        private final MunicipalityRepository municipalityRepository;
        private final ParishRepository parishRepository;
        private final ArtCategoryRepository artCategoryRepository;
        private final ArtDisciplineRepository artDisciplineRepository;

        public CultistService(CultistRepository cultistRepository, MunicipalityRepository municipalityRepository,
                        ParishRepository parishRepository, ArtCategoryRepository artCategoryRepository,
                        ArtDisciplineRepository artDisciplineRepository) {
                this.cultistRepository = cultistRepository;
                this.municipalityRepository = municipalityRepository;
                this.parishRepository = parishRepository;
                this.artCategoryRepository = artCategoryRepository;
                this.artDisciplineRepository = artDisciplineRepository;
        }

        public ResponseEntity<CultistResponse> create(CultistRequest cultistRequest) {
                if (cultistRepository.existsByIdNumber(cultistRequest.getIdNumber()))
                        throw new DuplicateEntityException("Id Number Already Exists");
                if (cultistRepository.existsByPhoneNumber(cultistRequest.getPhoneNumber()))
                        throw new DuplicateEntityException("Phone Number Already Exists");
                if (cultistRepository.existsByEmail(cultistRequest.getEmail()))
                        throw new DuplicateEntityException("Email Already Exists");
                String instagramUser = cultistRequest.getInstagramUser();
                if (instagramUser != null && !instagramUser.trim().isEmpty()) {
                        instagramUser = instagramUser.toLowerCase();
                        if (cultistRepository.existsByInstagramUser(cultistRequest.getInstagramUser()))
                                throw new DuplicateEntityException("Instagram Username Already Exists");
                }
                Cultist cultist = toCultist(cultistRequest);
                Cultist saved = cultistRepository.save(cultist);
                return ResponseEntity.ok(toCultistResponse(saved));
        }

        public List<CultistResponse> getAllCultistsWithFilters(String query, String gender, Integer municipalityId,
                        Integer parishId,
                        Integer artCategoryId, Integer artDisciplineId,
                        Boolean hasDisability, Boolean hasIllness) {
                Specification<Cultist> specification = CultistSpecification.withFilters(query,
                                gender, municipalityId, parishId, artCategoryId, artDisciplineId, hasDisability,
                                hasIllness);

                return cultistRepository.findAll(specification)
                                .stream()
                                .map(this::toCultistResponse)
                                .collect(Collectors.toList());
        }

        private Cultist toCultist(CultistRequest cultistRequest) {
                String firstName = cultistRequest.getFirstName();
                if (!StringValidator.isValidName(firstName))
                        throw new IllegalArgumentException("FirstName Is Invalid");
                firstName = StringUtils.toCapitalize(firstName);
                String lastName = cultistRequest.getLastName();
                if (!StringValidator.isValidName(lastName))
                        throw new IllegalArgumentException("LastName Is Invalid");
                lastName = StringUtils.toCapitalize(lastName);
                String gender = cultistRequest.getGender().trim().toUpperCase();
                if (!gender.equals("F") && !gender.equals("M"))
                        throw new IllegalArgumentException("Gender Is Invalid");
                String idNumber = cultistRequest.getIdNumber().trim();
                LocalDate birthDate = cultistRequest.getBirthDate();
                if (!DateValidator.isValidBirthDate(birthDate))
                        throw new IllegalArgumentException("BirthDate Is Invalid");
                String phoneNumber = cultistRequest.getPhoneNumber().trim();
                String email = cultistRequest.getEmail().trim().toLowerCase();
                String instagramUser = cultistRequest.getInstagramUser();
                if (instagramUser == null || instagramUser.isBlank()) {
                        instagramUser = null;
                } else {
                        instagramUser = instagramUser.trim().toLowerCase();
                }
                Municipality municipality = municipalityRepository.findById(cultistRequest.getMunicipalityId())
                                .orElseThrow(() -> new EntityNotFoundException("Municipality Not Found"));
                Parish parish = parishRepository.findById(cultistRequest.getParishId())
                                .orElseThrow(() -> new EntityNotFoundException("Parish Not Found"));
                validateParishId(parish, cultistRequest);
                String homeAddress = cultistRequest.getHomeAddress();
                homeAddress = StringUtils.toCapitalize(homeAddress);
                ArtCategory artCategory = artCategoryRepository.findById(cultistRequest.getArtCategoryId())
                                .orElseThrow(() -> new EntityNotFoundException("Art Category Not Found"));
                ArtDiscipline artDiscipline = artDisciplineRepository.findById(cultistRequest.getArtDisciplineId())
                                .orElseThrow(() -> new EntityNotFoundException("Art Discipline Not Found"));
                validateArtDisciplineId(artDiscipline, cultistRequest);
                int yearsofExperience = cultistRequest.getYearsOfExperience();
                String groupName = cultistRequest.getGroupName();
                if (groupName == null || groupName.isBlank()) {
                        groupName = null;
                } else {
                        groupName = StringUtils.toCapitalize(groupName);
                }
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

        private void validateParishId(Parish parish, CultistRequest cultistRequest) {
                if (parish.getMunicipality().getId() != cultistRequest.getMunicipalityId()) {
                        throw new IllegalArgumentException(
                                        "The Selected Parish Does Not Belong To The Chosen Municipality");
                }
        }

        private void validateArtDisciplineId(ArtDiscipline artDiscipline, CultistRequest cultistRequest) {
                if (artDiscipline.getaArtCategory().getId() != cultistRequest.getArtCategoryId()) {
                        throw new IllegalArgumentException(
                                        "The Selected Discipline Does Not Belong To The Chosen Category");
                }
        }

}
