package edu.ap.softwareproject.api.helpers;

import edu.ap.softwareproject.api.security.JWTGenerator;

public final class JWTHelper {
    private static final JWTGenerator jwt = new JWTGenerator();
    public static String generateDummyJWT() {
        return jwt.generateDummyToken("administrator@example.com");
    }
    public static String generateDummyJWT(String email) {
        return jwt.generateDummyToken(email);
    }
}
