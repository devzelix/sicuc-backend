package com.culturacarabobo.sicuc.backend.entities;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity // Specifies that this class is a JPA entity
@EntityListeners(AuditingEntityListener.class) // Enables auditing listener for automatic timestamp management
@Table(name = "cultors") // Maps to the "cultors" table in the database
public class Cultor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated primary key
    private int id;

    @Column(length = 50, nullable = false) // First name, required, max length 50
    private String firstName;

    @Column(length = 50, nullable = false) // Last name, required, max length 50
    private String lastName;

    @Column(length = 1, nullable = false) // Gender (e.g., M/F/O), required, 1 character
    private String gender;

    @Column(length = 10, nullable = false, unique = true) // National ID number, required and unique
    private String idNumber;

    @Column(nullable = false) // Birth date, required
    private LocalDate birthDate;

    @Column(length = 12, nullable = false, unique = true) // Phone number, required and unique
    private String phoneNumber;

    @Column(length = 150, unique = true) // Optional Email, unique if provided
    private String email;

    @Column(length = 30, unique = true) // Optional Instagram username, unique if provided
    private String instagramUser;

    @ManyToOne(fetch = FetchType.LAZY) // Many cultors belong to one municipality
    @JoinColumn(name = "municipality_id", nullable = false) // Foreign key to Municipality
    private Municipality municipality;

    @ManyToOne(fetch = FetchType.LAZY) // Many cultors belong to one parish
    @JoinColumn(name = "parish_id", nullable = false) // Foreign key to Parish
    private Parish parish;

    @Column(length = 100, nullable = false) // Full home address, required
    private String homeAddress;

    @ManyToOne(fetch = FetchType.LAZY) // Many cultors can belong to one art category
    @JoinColumn(name = "art_category_id", nullable = false) // Foreign key to ArtCategory
    private ArtCategory artCategory;

    @ManyToOne(fetch = FetchType.LAZY) // Many cultors can belong to one art discipline
    @JoinColumn(name = "art_discipline_id", nullable = false) // Foreign key to ArtDiscipline
    private ArtDiscipline artDiscipline;

    @Column(length = 100) // Optional field for custom discipline when "Other" is selected
    private String otherDiscipline;

    @Column(nullable = false) // Required years of experience in their field
    private int yearsOfExperience;

    @Column(length = 100) // Optional group affiliation
    private String groupName;

    @Column(length = 100) // Optional field for recording disability
    private String disability;

    @Column(length = 100) // Optional field for recording illness
    private String illness;

    @CreatedDate // Automatically assigned creation date and time on entity persist
    @Column(nullable = false, updatable = false) // Cannot be null or updated once set
    private LocalDate createdAt;

    /**
     * Default constructor for JPA.
     */
    public Cultor() {
    }

    /**
     * All-arguments constructor to facilitate object creation.
     */
    public Cultor(String firstName, String lastName, String gender, String idNumber, LocalDate birthDate,
            String phoneNumber, String email, String instagramUser, Municipality municipality, Parish parish,
            String homeAddress, ArtCategory artCategory, ArtDiscipline artDiscipline, String otherDiscipline,
            int yearsOfExperience, String groupName, String disability, String illness) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.idNumber = idNumber;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.instagramUser = instagramUser;
        this.municipality = municipality;
        this.parish = parish;
        this.homeAddress = homeAddress;
        this.artCategory = artCategory;
        this.artDiscipline = artDiscipline;
        this.otherDiscipline = otherDiscipline;
        this.yearsOfExperience = yearsOfExperience;
        this.groupName = groupName;
        this.disability = disability;
        this.illness = illness;
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

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public Parish getParish() {
        return parish;
    }

    public void setParish(Parish parish) {
        this.parish = parish;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public ArtCategory getArtCategory() {
        return artCategory;
    }

    public void setArtCategory(ArtCategory artCategory) {
        this.artCategory = artCategory;
    }

    public ArtDiscipline getArtDiscipline() {
        return artDiscipline;
    }

    public void setArtDiscipline(ArtDiscipline artDiscipline) {
        this.artDiscipline = artDiscipline;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

}
