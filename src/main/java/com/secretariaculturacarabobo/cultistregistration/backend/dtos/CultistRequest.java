package com.secretariaculturacarabobo.cultistregistration.backend.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CultistRequest {

    @NotBlank(message = "Is Requerid")
    @Size(max = 50, message = "Must Have A Maximum Of 50 Characters")
    private String firstName;
    @NotBlank(message = "Is Requerid")
    @Size(max = 50, message = "Must Have A Maximum Of 50 Characters")
    private String lastName;
    @NotBlank(message = "Is Requerid")
    @Size(max = 10, message = "Must Have A Maximum Of 10 Characters")
    @Pattern(regexp = "^[VE]-\\d{1,8}$", message = "Is Invalid")
    private String idNumber;
    @NotNull(message = "Is Requerid")
    private LocalDate birthDate;
    @NotBlank(message = "Is Requerid")
    @Size(min = 12, max = 12, message = "Must Be 12 Characters")
    @Pattern(regexp = "^04(12|14|16|24|26)-\\d{7}$", message = "Is Invalid")
    private String phoneNumber;
    @NotBlank(message = "Is Requerid")
    @Size(max = 150, message = "Must Have A Maximum Of 150 Characters")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]{1,184}\\.[a-zA-Z]{2,10}$", message = "Is Invalid")
    private String email;
    @NotBlank(message = "Is Requerid")
    @Size(max = 30, message = "Must Have A Maximum Of 30 Characters")
    @Pattern(regexp = "^(?!.*[._]{2})[a-zA-Z0-9](?:[a-zA-Z0-9._]{0,28}[a-zA-Z0-9])?$", message = "Is Invalid")
    private String instagramUser;
    @Min(1)
    private int municipalityId;
    @Min(1)
    private int parishId;
    @NotBlank(message = "Is Requerid")
    @Size(max = 100, message = "Must Have A Maximum Of 100 Characters")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{1,100}$", message = "Is Invalid")
    private String homeAddress;
    @Min(1)
    private int artCategoryId;
    @Min(1)
    private int artDisciplineId;
    @Min(1)
    @Max(100)
    private int yearsOfExperience;
    @Size(max = 100)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{0,100}$", message = "Is Invalid")
    private String groupName;
    @Size(max = 100, message = "Must Have A Maximum Of 100 Characters")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{0,100}$", message = "Is Invalid")
    private String disability;
    @Size(max = 100, message = "Must Have A Maximum Of 100 Characters")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{0,100}$", message = "Is Invalid")
    private String illness;

    public CultistRequest(String firstName, String lastName, String idNumber, LocalDate birthDate, String phoneNumber,
            String email, String instagramUser, int municipalityId, int parishId, String homeAddress, int artCategoryId,
            int artDisciplineId, int yearsOfExperience, String groupName, String disability, String illness) {
        this.firstName = firstName;
        this.lastName = lastName;
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
