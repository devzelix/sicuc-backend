package com.culturacarabobo.sicuc.backend.dtos;

import java.time.LocalDate;

/**
 * DTO for sending cultor data back to the client.
 */
public class CultorResponse {

    // Unique database ID
    private int id;

    // First name
    private String firstName;

    // Last name
    private String lastName;

    // Gender
    private String gender;

    // National ID number (e.g., V-12345678)
    private String idNumber;

    // Date of birth
    private LocalDate birthDate;

    // Phone number (e.g., 0412-1234567)
    private String phoneNumber;

    // Email address (optional)
    private String email;

    // Instagram username (optional)
    private String instagramUser;

    // Municipality ID
    private int municipalityId;

    // Parish ID
    private int parishId;

    // Full home address
    private String homeAddress;

    // Art category ID
    private int artCategoryId;

    // Art discipline ID
    private int artDisciplineId;

    // Total years of experience in the field
    private int yearsOfExperience;

    // Artistic group name (optional)
    private String groupName;

    // Disability description (optional)
    private String disability;

    // Illness description (optional)
    private String illness;

    // Creation date
    private LocalDate createdAt;

    // Constructor with all fields
    public CultorResponse(int id, String firstName, String lastName, String gender, String idNumber,
            LocalDate birthDate,
            String phoneNumber,
            String email, String instagramUser, int municipalityId, int parishId, String homeAddress, int artCategoryId,
            int artDisciplineId, int yearsOfExperience, String groupName, String disability, String illness,
            LocalDate createdAt) {
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
        this.yearsOfExperience = yearsOfExperience;
        this.groupName = groupName;
        this.disability = disability;
        this.illness = illness;
        this.createdAt = createdAt;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstagramUser() {
        return instagramUser;
    }

    public void setInstagramUser(String instagramUser) {
        this.instagramUser = instagramUser;
    }

    public int getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(int municipalityId) {
        this.municipalityId = municipalityId;
    }

    public int getParishId() {
        return parishId;
    }

    public void setParishId(int parishId) {
        this.parishId = parishId;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public int getArtCategoryId() {
        return artCategoryId;
    }

    public void setArtCategoryId(int artCategoryId) {
        this.artCategoryId = artCategoryId;
    }

    public int getArtDisciplineId() {
        return artDisciplineId;
    }

    public void setArtDisciplineId(int artDisciplineId) {
        this.artDisciplineId = artDisciplineId;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

}
