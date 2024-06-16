package edu.ap.softwareproject.api.config;

import edu.ap.softwareproject.api.util.SentryExceptionHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A bean for the sentry exception helper.
 */
@Configuration
public class SentryExceptionHelperBean {
  @Bean
  public SentryExceptionHelper sentryExceptionHelper() {
    return new SentryExceptionHelper();
  }
}
