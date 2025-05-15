package com.secretariaculturacarabobo.cultistregistration.backend.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cultists")
public class Cultist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String firstName;
    @NotNull
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String lastName;
    @NotNull
    @Size(max = 10)
    @Column(length = 10, nullable = false)
    private String idNumber;
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date birthDate;
    @NotNull
    @Size(max = 12)
    @Column(length = 12, nullable = false)
    private String phoneNumber;
    @NotNull
    @Email
    @Column(length = 100, nullable = false)
    private String email;
    @Size(max = 30)
    @Column(length = 30, nullable = true)
    private String instagramUser;
    @ManyToOne
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;
    @ManyToOne
    @JoinColumn(name = "parish_id", nullable = false)
    private Parish parish;
    @NotNull
    @Size(min = 5, max = 100)
    @Column(length = 100, nullable = false)
    private String homeAddress;
    @ManyToOne
    @JoinColumn(name = "art_category_id", nullable = false)
    private ArtCategory artCategory;
    @ManyToOne
    @JoinColumn(name = "art_discipline_id", nullable = false)
    private ArtDiscipline artDiscipline;
    @Min(1)
    @Max(100)
    @Column(nullable = false)
    private byte yearsOfExperience;
    @Size(max = 100)
    @Column(length = 100, nullable = true)
    private String disability;
    @Size(max = 100)
    @Column(length = 100, nullable = true)
    private String illness;

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
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

    public byte getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(byte yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
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
