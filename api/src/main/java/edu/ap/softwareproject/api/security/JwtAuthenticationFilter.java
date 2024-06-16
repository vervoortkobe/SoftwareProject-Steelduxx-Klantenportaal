package edu.ap.softwareproject.api.security;

import edu.ap.softwareproject.api.util.AuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JWTGenerator jwtGenerator;
  private final CustomAccountDetailsService customAccountDetailsService;

  @Autowired
  public JwtAuthenticationFilter(JWTGenerator jwtGenerator, CustomAccountDetailsService customAccountDetailsService) {
    this.jwtGenerator = jwtGenerator;
    this.customAccountDetailsService = customAccountDetailsService;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    Optional<Cookie> cookie = AuthUtil.getJWTToken(request.getCookies());
    if (cookie.isPresent() && StringUtils.hasText(cookie.get().getValue())
        && jwtGenerator.validateToken(cookie.get().getValue())) {
      String email = jwtGenerator.getEmailFromJWT(cookie.get().getValue());

      UserDetails userDetails = customAccountDetailsService.loadUserByUsername(email);
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails, null,
          userDetails.getAuthorities());
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    } else {
      Cookie clearCookie = new Cookie("JWT", "");
      clearCookie.setMaxAge(0);
      response.addCookie(clearCookie);
    }
    filterChain.doFilter(request, response);
  }
}
