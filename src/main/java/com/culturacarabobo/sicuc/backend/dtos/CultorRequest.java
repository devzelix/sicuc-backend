package com.culturacarabobo.sicuc.backend.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for {@link com.culturacarabobo.sicuc.backend.entities.Cultor}
 * creation and update requests.
 * <p>
 * This class uses Jakarta Validation (@Valid) to enforce data integrity
 * for all incoming data from the client.
 */
public class CultorRequest {

    /**
     * The cultor's first name.
     * Required, max 50 characters.
     */
    @NotBlank(message = "Is Required")
    @Size(max = 50, message = "Must Have A Maximum Of 50 Characters")
    private String firstName;

    /**
     * The cultor's last name.
     * Required, max 50 characters.
     */
    @NotBlank(message = "Is Required")
    @Size(max = 50, message = "Must Have A Maximum Of 50 Characters")
    private String lastName;

    /**
     * The cultor's gender (e.g., "M", "F").
     * Required, single character.
     */
    @NotBlank(message = "Is Required")
    @Size(max = 1)
    private String gender;

    /**
     * The cultor's national identification number.
     * Required, max 10 characters. Must match format (e.g., "V-12345678" or "E-87654321").
     */
    @NotBlank(message = "Is Required")
    @Size(max = 10, message = "Must Have A Maximum Of 10 Characters")
    @Pattern(regexp = "^[VE]-\\d{1,8}$", message = "Is Invalid")
    private String idNumber;

    /**
     * The cultor's date of birth.
     * Required. Must be a valid date.
     */
    @NotNull(message = "Is Required")
    private LocalDate birthDate;

    /**
     * The cultor's primary phone number.
     * Required. Must match format "04XX-XXXXXXX" (e.g., "0412-1234567").
     */
    @NotBlank(message = "Is Required")
    @Size(min = 12, max = 12, message = "Must Be 12 Characters")
    @Pattern(regexp = "^04(12|14|16|24|26)-\\d{7}$", message = "Is Invalid")
    private String phoneNumber;

    /**
     * The cultor's email address.
     * Optional. Must be a valid email format if provided. Max 150 characters.
     */
    @Size(max = 150, message = "Must Have A Maximum Of 150 Characters")
    @Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]{1,184}\\.[a-zA-Z]{2,10}$", message = "Is Invalid")
    private String email;

    /**
     * The cultor's Instagram handle.
     * Optional. Must be a valid username format if provided. Max 30 characters.
     */
    @Size(max = 30, message = "Must Have A Maximum Of 30 Characters")
    @Pattern(regexp = "^$|^(?!.*[._]{2})[a-zA-Z0-9](?:[a-zA-Z0-9._]{0,28}[a-zA-Z0-9])?$", message = "Is Invalid")
    private String instagramUser;

    /**
     * The foreign key ID of the cultor's {@link com.culturacarabobo.sicuc.backend.entities.Municipality}.
     * Required. Must be a valid ID > 0.
     */
    @Min(1)
    private int municipalityId;

    /**
     * The foreign key ID of the cultor's {@link com.culturacarabobo.sicuc.backend.entities.Parish}.
     * Required. Must be a valid ID > 0.
     */
    @Min(1)
    private int parishId;

    /**
     * The cultor's full home address.
     * Required. Max 100 characters.
     */
    @NotBlank(message = "Is Required")
    @Size(max = 100, message = "Must Have A Maximum Of 100 Characters")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{1,100}$", message = "Is Invalid")
    private String homeAddress;

    /**
     * The foreign key ID of the cultor's {@link com.culturacarabobo.sicuc.backend.entities.ArtCategory}.
     * Required. Must be a valid ID > 0.
     */
    @Min(1)
    private int artCategoryId;

    /**
     * The foreign key ID of the cultor's {@link com.culturacarabobo.sicuc.backend.entities.ArtDiscipline}.
     * Required. Must be a valid ID > 0.
     */
    @Min(1)
    private int artDisciplineId;

    /**
     * A free-text field for a custom discipline.
     * Optional. Only used if the selected 'artDisciplineId' is "Otra...". Max 100
     * characters.
     */
    @Size(max = 100, message = "Must Have A Maximum Of 100 Characters")
    @Pattern(regexp = "^$|[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{0,100}$", message = "Is Invalid")
    private String otherDiscipline;

    /**
     * The number of years the cultor has been active in their field.
     * Required. Must be between 1 and 100.
     */
    @Min(1)
    @Max(100)
    private int yearsOfExperience;

    /**
     * The name of any group, collective, or association the cultor belongs to.
     * Optional. Max 100 characters.
     */
    @Size(max = 100)
    @Pattern(regexp = "^$|[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{0,100}$", message = "Is Invalid")
    private String groupName;

    /**
     * Description of any disability the cultor has.
     * Optional. Max 100 characters.
     */
    @Size(max = 100, message = "Must Have A Maximum Of 100 Characters")
    @Pattern(regexp = "^$|[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{0,100}$", message = "Is Invalid")
    private String disability;

    /**
     * Description of any chronic illness the cultor has.
     * Optional. Max 100 characters.
     */
    @Size(max = 100, message = "Must Have A Maximum Of 100 Characters")
    @Pattern(regexp = "^$|[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s\\-',.]{0,100}$", message = "Is Invalid")
    private String illness;

    /**
     * All-arguments constructor used for mapping and testing.
     */
    public CultorRequest(String firstName, String lastName, String gender, String idNumber, LocalDate birthDate,
            String phoneNumber, String email, String instagramUser, int municipalityId, int parishId,
            String homeAddress, int artCategoryId, int artDisciplineId, String otherDiscipline, int yearsOfExperience,
            String groupName, String disability, String illness) {
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
    }

    // --- Standard Getters and Setters ---

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

    public String getOtherDiscipline() {
        return otherDiscipline;
    }

    public void setOtherDiscipline(String otherDiscipline) {
        this.otherDiscipline = otherDiscipline;
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