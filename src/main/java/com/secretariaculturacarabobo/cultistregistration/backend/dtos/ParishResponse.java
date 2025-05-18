package com.secretariaculturacarabobo.cultistregistration.backend.dtos;

public class ParishResponse {

    private int id;
    private String name;
    private int municipalityId;

    public ParishResponse(int id, String name, int municipalityId) {
        this.id = id;
        this.name = name;
        this.municipalityId = municipalityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(int municipalityId) {
        this.municipalityId = municipalityId;
    }

}
