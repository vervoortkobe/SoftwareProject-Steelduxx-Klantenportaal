package edu.ap.softwareproject.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JWTGenerator {
  @Value("${expiredays}")
  private Long expiredays;
  final SecretKey secretKey = Keys.hmacShaKeyFor(System.getenv("SECRET_KEY").getBytes());

  public String generateToken(Authentication authentication) {
    String email = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + TimeUnit.DAYS.toMillis(expiredays));

    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(expireDate)
        .signWith(secretKey)
        .compact();
  }

  public String getEmailFromJWT(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //ONLY USE THIS FOR INTEGRATION TESTS, PLEASE.
    public String generateDummyToken(String email) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + 86400000);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }
}