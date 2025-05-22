package com.secretariaculturacarabobo.cultistregistration.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "Is Required")
    @Size(max = 25, message = "Must Have A Maximum Of 25 Characters")
    private String username;
    @NotBlank(message = "Is Required")
    @Size(max = 255, message = "Must Have A Maximum Of 255 Characters")
    private String password;

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
