package edu.ap.softwareproject.api.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * A bearer token class used for making requests to the Steelduxx API.
 */
@Getter
public class BearerToken implements Serializable {

    private String token;

    public BearerToken() {
    }
    
    public BearerToken(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
