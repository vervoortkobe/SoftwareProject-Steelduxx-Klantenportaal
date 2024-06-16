package edu.ap.softwareproject.api.dto;

import lombok.Getter;

@Getter
public class LoginDTO {
    // ---- GETTERS AND SETTERS ----
    // ---- ATTRIBUTES ----
    private String email;
    private String password;

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }
}
