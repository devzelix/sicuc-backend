package com.secretariaculturacarabobo.cultistregistration.backend.dtos;

public class ArtDisciplineResponse {

    private int id;
    private String name;
    private int artCategoryId;

    public ArtDisciplineResponse(int id, String name, int artCategoryId) {
        this.id = id;
        this.name = name;
        this.artCategoryId = artCategoryId;
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

    public int getArtCategoryId() {
        return artCategoryId;
    }

    public void setArtCategoryId(int artCategoryId) {
        this.artCategoryId = artCategoryId;
    }

}
