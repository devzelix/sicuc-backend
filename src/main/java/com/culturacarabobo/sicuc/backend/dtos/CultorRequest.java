package com.culturacarabobo.sicuc.backend.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for incoming Cultor registration requests.
 */
public class CultorRequest {

    @NotBlank(message = "Is Required")
    @Size(max = 50, message = "Must Have A Maximum Of 50 Characters")
    private String firstName; // Cultor's first name
    @NotBlank(message = "Is Required")
    @Size(max = 50, message = "Must Have A Maximum Of 50 Characters")
    private String lastName; // Cultor's last name
    @NotBlank(message = "Is Required")
    @Size(max = 1)
    private String gender; // Gender (single character)
    @NotBlank(message = "Is Required")
    @Size(max = 10, message = "Must Have A Maximum Of 10 Characters")
    @Pattern(regexp = "^[VE]-\\d{1,8}$", message = "Is Invalid")
    private String idNumber; // Identification number, format e.g. V-12345678
    @NotNull(message = "Is Required")
    private LocalDate birthDate;
    @NotBlank(message = "Is Required")
    @Size(min = 12, max = 12, message = "Must Be 12 Characters")
    @Pattern(regexp = "^04(12|14|16|24|26)-\\d{7}$", message = "Is Invalid")
    private String phoneNumber; // Phone number, format e.g. 0412-1234567
    @Size(max = 150, message = "Must Have A Maximum Of 150 Characters")
    @Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]{1,184}\\.[a-zA-Z]{2,10}$", message = "Is Invalid")
    private String email; // Optional Email address
    @Size(max = 30, message = "Must Have A Maximum Of 30 Characters")
    @Pattern(regexp = "^$|^(?!.*[._]{2})[a-zA-Z0-9](?:[a-zA-Z0-9._]{0,28}[a-zA-Z0-9])?$", message = "Is Invalid")
    private String instagramUser; // Optional Instagram username
    @Min(1)
    private int municipalityId; // Associated municipality ID
    @Min(1)
    private int parishId; // Associated parish ID
    @NotBlank(message = "Is Required")
    @Size(max = 100, message = "Must Have A Maximum Of 100 Characters")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{1,100}$", message = "Is Invalid")
    private String homeAddress; // Home address
    @Min(1)
    private int artCategoryId; // Associated art category ID
    @Min(1)
    private int artDisciplineId; // Associated art discipline ID
    @Min(1)
    @Max(100)
    private int yearsOfExperience; // Years of experience (1-100)
    @Size(max = 100)
    @Pattern(regexp = "^$|[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{0,100}$", message = "Is Invalid")
    private String groupName; // Optional artistic group name
    @Size(max = 100, message = "Must Have A Maximum Of 100 Characters")
    @Pattern(regexp = "^$|[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{0,100}$", message = "Is Invalid")
    private String disability; // Optional disability description
    @Size(max = 100, message = "Must Have A Maximum Of 100 Characters")
    @Pattern(regexp = "^$|[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{0,100}$", message = "Is Invalid")
    private String illness; // Optional illness description

    // Constructor with all fields
    public CultorRequest(String firstName, String lastName, String gender, String idNumber, LocalDate birthDate,
            String phoneNumber,
            String email, String instagramUser, int municipalityId, int parishId, String homeAddress, int artCategoryId,
            int artDisciplineId, int yearsOfExperience, String groupName, String disability, String illness) {
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
    }

    // Getters and setters

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

}
