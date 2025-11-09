package com.culturacarabobo.sicuc.backend.dtos;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for sending {@link com.culturacarabobo.sicuc.backend.entities.Cultor}
 * data to the client.
 * <p>
 * This class is an immutable data carrier. It is populated by the service
 * layer and serialized to JSON for the API response.
 */
public final class CultorResponse {

    /** The unique database identifier (primary key). */
    private final int id;

    /** The cultor's first name. */
    private final String firstName;

    /** The cultor's last name. */
    private final String lastName;

    /** The cultor's gender (e.g., "M", "F"). */
    private final String gender;

    /** The cultor's national identification number (e.g., "V-12345678"). */
    private final String idNumber;

    /** The cultor's date of birth. */
    private final LocalDate birthDate;

    /** The cultor's primary phone number. */
    private final String phoneNumber;

    /** The cultor's email address (optional). */
    private final String email;

    /** The cultor's Instagram handle (optional). */
    private final String instagramUser;

    /** The foreign key ID of the associated Municipality. */
    private final int municipalityId;

    /** The foreign key ID of the associated Parish. */
    private final int parishId;

    /** The cultor's full home address. */
    private final String homeAddress;

    /** The foreign key ID of the associated ArtCategory. */
    private final int artCategoryId;

    /** The foreign key ID of the associated ArtDiscipline. */
    private final int artDisciplineId;

    /** A free-text field for a custom discipline (optional). */
    private final String otherDiscipline;

    /** Total years of experience in the field. */
    private final int yearsOfExperience;

    /** The name of any group the cultor belongs to (optional). */
    private final String groupName;

    /** Description of any disability (optional). */
    private final String disability;

    /** Description of any chronic illness (optional). */
    private final String illness;

    /** The timestamp of when the cultor was registered. */
    private final LocalDate createdAt;

    /**
     * All-arguments constructor for creating the response DTO.
     * This is typically called by the service layer during entity-to-DTO mapping.
     */
    public CultorResponse(int id, String firstName, String lastName, String gender, String idNumber,
            LocalDate birthDate, String phoneNumber, String email, String instagramUser, int municipalityId,
            int parishId, String homeAddress, int artCategoryId, int artDisciplineId, String otherDiscipline,
            int yearsOfExperience, String groupName, String disability, String illness, LocalDate createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.idNumber = idNumber;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.instagramUser = instagramUser;
        this.municipalityId = municipalityId;
        this.parishId = parishId;
        this.homeAddress = homeAddress;
        this.artCategoryId = artCategoryId;
        this.artDisciplineId = artDisciplineId;
        this.otherDiscipline = otherDiscipline;
        this.yearsOfExperience = yearsOfExperience;
        this.groupName = groupName;
        this.disability = disability;
        this.illness = illness;
        this.createdAt = createdAt;
    }

    // --- Standard Getters ---
    // (No setters are provided, as this is an immutable DTO)

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getInstagramUser() {
        return instagramUser;
    }

    public int getMunicipalityId() {
        return municipalityId;
    }

    public int getParishId() {
        return parishId;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public int getArtCategoryId() {
        return artCategoryId;
    }

    public int getArtDisciplineId() {
        return artDisciplineId;
    }

    public String getOtherDiscipline() {
        return otherDiscipline;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDisability() {
        return disability;
    }

    public String getIllness() {
        return illness;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}