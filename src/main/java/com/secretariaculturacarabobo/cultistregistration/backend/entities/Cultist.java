package com.secretariaculturacarabobo.cultistregistration.backend.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cultists")
public class Cultist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 50, nullable = false)
    private String firstName;
    @Column(length = 50, nullable = false)
    private String lastName;
    @Column(length = 10, nullable = false, unique = true)
    private String idNumber;
    @Column(nullable = false)
    private LocalDate birthDate;
    @Column(length = 12, nullable = false, unique = true)
    private String phoneNumber;
    @Column(length = 150, nullable = false, unique = true)
    private String email;
    @Column(length = 30, nullable = true, unique = true)
    private String instagramUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parish_id", nullable = false)
    private Parish parish;
    @Column(length = 100, nullable = false)
    private String homeAddress;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_category_id", nullable = false)
    private ArtCategory artCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_discipline_id", nullable = false)
    private ArtDiscipline artDiscipline;
    @Column(nullable = false)
    private int yearsOfExperience;
    @Column(length = 100, nullable = true)
    private String groupName;
    @Column(length = 100, nullable = true)
    private String disability;
    @Column(length = 100, nullable = true)
    private String illness;

    public Cultist() {
    }

    public Cultist(String firstName, String lastName, String idNumber, LocalDate birthDate, String phoneNumber,
            String email, Municipality municipality, Parish parish, String homeAddress, ArtCategory artCategory,
            ArtDiscipline artDiscipline, int yearsOfExperience, String groupName, String disability, String illness) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.municipality = municipality;
        this.parish = parish;
        this.homeAddress = homeAddress;
        this.artCategory = artCategory;
        this.artDiscipline = artDiscipline;
        this.yearsOfExperience = yearsOfExperience;
        this.groupName = groupName;
        this.disability = disability;
        this.illness = illness;

    }

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
