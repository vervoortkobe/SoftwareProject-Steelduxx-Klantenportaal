package edu.ap.softwareproject.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * A configuration class for CORS on localhost.
 */
@Configuration
@EnableWebMvc
public class DevConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    String domain = "*://localhost:4200";

    if (System.getenv("DOMAIN") != null)
      domain = "*://" + System.getenv("DOMAIN");

    registry.addMapping("/**")
        .allowedOriginPatterns(domain)
        .allowCredentials(true)
        .allowedMethods("*")
        .allowedHeaders("*");
  }
}
