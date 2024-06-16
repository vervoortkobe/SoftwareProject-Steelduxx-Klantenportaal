package edu.ap.softwareproject.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import edu.ap.softwareproject.api.enums.Role;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  private final JwtAuthEntryPoint authEntryPoint;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfiguration(JwtAuthEntryPoint authEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter,
      CustomAccountDetailsService accountDetailsService) {
    this.authEntryPoint = authEntryPoint;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.accountDetailsService = accountDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // DISABLE X-FRAME-OPTIONS FOR H2 CONSOLE.
    // UNCOMMENT IF YOU WANT TO USE THE H2 CONSOLE.
    // http.headers(headers ->
    // headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    // set the name of the attribute the CsrfToken will be populated on
    requestHandler.setCsrfRequestAttributeName("XSRF-TOKEN");

    http.csrf(csrf -> {
      csrf.ignoringRequestMatchers("/countries/**", "/password-reset/**", "/h2/**", "/error", "/users/**")
          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
          .csrfTokenRequestHandler(requestHandler);
    });
    http.csrf(csrf -> csrf.disable());
    http
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/error").permitAll()
            .requestMatchers("/users/**").permitAll()
            .requestMatchers("/h2/**").permitAll()
            .requestMatchers("/password-reset/**").permitAll()
            .requestMatchers("/countries/**").permitAll()
            .requestMatchers("/admin/**").hasRole(Role.BEHEERDER.name())
            .requestMatchers("/admincodes").hasRole(Role.BEHEERDER.name())
            .anyRequest().authenticated())
        .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authEntryPoint))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(
            sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  private final CustomAccountDetailsService accountDetailsService;

  @Bean
  public UserDetailsService userDetailsService() {
    return accountDetailsService;
  }

  @Bean
  AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
