package edu.ap.softwareproject.api.util;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;

public final class AuthUtil {
  /**
   * Extracts the JWT token from a given cookie array.
   * @param cookies An array containing cookies from the browser.
   * @return A JWT token.
   */
  public static Optional<Cookie> getJWTToken(Cookie[] cookies) {
    if (cookies != null && cookies.length > 0)
      return Arrays.stream(cookies).filter(e -> e.getName().equals("JWT")).findFirst();
    return Optional.empty();
  }
}
