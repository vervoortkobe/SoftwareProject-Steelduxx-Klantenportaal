package edu.ap.softwareproject.api;

import edu.ap.softwareproject.api.interfaces.ExceptionHelper;
import edu.ap.softwareproject.api.util.SentryExceptionHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.sentry.Sentry;

@SpringBootApplication
public class ApiApplication {
	public static final ExceptionHelper sentry = new SentryExceptionHelper();

	public static void main(String[] args) {
		Sentry.init(options -> options.setEnableExternalConfiguration(true));

		SpringApplication.run(ApiApplication.class, args);
	}

}
