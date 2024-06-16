package edu.ap.softwareproject.api.config;

import edu.ap.softwareproject.api.security.JWTGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A bean for the the JWT generator.
 */
@Configuration
public class JWTGeneratorBean {
  @Bean
  public JWTGenerator jwtGenerator() {
    return new JWTGenerator();
  }
}
