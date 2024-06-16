package edu.ap.softwareproject.api.util;

import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.dto.BearerToken;
import edu.ap.softwareproject.api.enums.Role;
import edu.ap.softwareproject.api.interfaces.ExceptionHelper;

import static edu.ap.softwareproject.api.ApiApplication.sentry;

@Component
public class ShipmentRepositoryUtil {

  @Getter
  private static BearerToken bearerToken = new BearerToken();
  @Getter
  private static BearerToken adminBearerToken = new BearerToken();
  private static final ObjectMapper mapper = new ObjectMapper();
  private static ExceptionHelper sentryExceptionHelper;
  private static final HttpClient http = HttpClient.newHttpClient();

  public static final String BASE_URL = "https://sw11-1.devops-ap.be";
  private static String apiSecretAdminGroup;
  private static String apiSecretAdminKey;

  private static final Map<String, BearerToken> bearerTokens = new HashMap<>();

  public ShipmentRepositoryUtil(SentryExceptionHelper exceptionHelper) {
    ShipmentRepositoryUtil.apiSecretAdminGroup = System.getenv("API_SECRET_GROUP");
    ShipmentRepositoryUtil.apiSecretAdminKey = System.getenv("API_SECRET_KEY");
    sentryExceptionHelper = exceptionHelper;
  }

  public static void setBearerToken(BearerToken bearerToken) {
    ShipmentRepositoryUtil.bearerToken = bearerToken;
  }

  public static void setAdminBearerToken(BearerToken adminBearerToken) {
    ShipmentRepositoryUtil.adminBearerToken = adminBearerToken;
  }

  // #region Create GET Request
  /**
   * Creates a new HTTP GET request with the given URL.
   *
   * @param url the URL for the request
   * @return the created HTTP GET request
   */
  public static HttpRequest createGetReq(Account account, String url) {
    if (url == null || url.isEmpty()) {
      throw new IllegalArgumentException("URL cannot be null or empty");
    }

    if (account.getRole() == Role.KLANT && bearerTokens.get(account.getCustomerCode()) == null) {
      refreshBearerToken(account);
    }

    if (bearerTokens.get(account.getCustomerCode()) != null || account.getRole().equals(Role.BEHEERDER)) {
      return HttpRequest.newBuilder()
          .setHeader("Authorization",
              "Bearer "
                  + (account
                      .getRole() == Role.KLANT ? bearerTokens
                          .get(account
                              .getCustomerCode())
                          .getToken()
                          : adminBearerToken.getToken()))
          .setHeader("Content-Type", "application/json")
          .uri(URI.create(url))
          .GET()
          .build();
    }
    return null;
  }
  public static HttpRequest createPostReq(Account account, String url, Object body) {
    if (url == null || url.isEmpty()) {
      throw new IllegalArgumentException("URL cannot be null or empty");
    }

    if (account.getRole() == Role.KLANT && bearerTokens.get(account.getCustomerCode()) == null) {
      refreshBearerToken(account);
    }

    if (bearerTokens.get(account.getCustomerCode()) != null || account.getRole().equals(Role.BEHEERDER)) {
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      try {
        String json = ow.writeValueAsString(body);

        return HttpRequest.newBuilder()
                .setHeader("Authorization",
                        "Bearer "
                                + (account
                                .getRole() == Role.KLANT ? bearerTokens
                                .get(account
                                        .getCustomerCode())
                                .getToken()
                                : adminBearerToken.getToken()))
                .setHeader("Content-Type", "application/json")
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
      } catch (JsonProcessingException ex) {
        sentryExceptionHelper.warn(ex);
      }
    }
    return null;
  }
  // #endregion

  // #region Create Response List Mapper
  /**
   * A method to create a list mapper for the given HTTP response.
   * 
   * @param account   The account of the requesting user.
   * @param res       the HTTP response containing the data to be mapped
   * @param valueType the type of the objects in the list
   * @return the list of Objects created from the response body
   */
  public static <T> Optional<List<T>> createResListMapper(Account account,
      HttpResponse<String> res,
      Class<T> valueType) {
    if (res.statusCode() == 403) {
      refreshBearerToken(account);
    }
    try {
      List<T> objects = mapper.readValue(res.body(),
          mapper.getTypeFactory().constructCollectionType(List.class, valueType));
      return Optional.of(objects);
    } catch (JsonProcessingException e) {
      sentryExceptionHelper.warn(e);
    }
    return Optional.empty();
  }
  // #endregion

  // #region Create Response Mapper
  /**
   * Creates an OrderDetailsDTO object by mapping the HttpResponse body to the
   * OrderDetailsDTO type.
   *
   * @param account The account of the requesting user.
   * @param res  the HttpResponse object containing the body to be mapped
   * @return the mapped OrderDetailsDTO object
   */
  public static <T> Optional<T> createResMapper(Account account,
      HttpResponse<String> res, Class<T> valueType) {

    if (res.statusCode() == 403) {
      refreshBearerToken(account);
    }
    try {

      T object = mapper.readValue(res.body(), valueType);
      return Optional.of(object);
    } catch (JsonProcessingException e) {
      sentryExceptionHelper.warn(e);
    }
    return Optional.empty();
  }
  // #endregion

  // #region Execute GET Request
  /**
   * A description of the entire Java function.
   *
   * @param req description of parameter
   * @return description of return value
   */
  public static HttpResponse<String> execReq(HttpRequest req) {
    try {
      HttpResponse<String> response = http.sendAsync(req, HttpResponse.BodyHandlers.ofString()).get();
      if (response.statusCode() != 200)
        sentry.infoReport("Response got a non-200! For URL: " + req.uri() + " with response: " + response);
      return response;
    } catch (InterruptedException | ExecutionException e) {
      sentryExceptionHelper.warn(e);
      Thread.currentThread().interrupt();
      return null;
    }
  }
  // #endregion

  // #region Refresh Bearer Token
  /**
   * Refreshes the bearer token by making a POST request to the authentication
   * endpoint with the provided API secret group and key.
   *
   * @return the refreshed bearer token, or null if the token could not be
   *         refreshed
   */
  public static BearerToken refreshBearerToken(Account account) {

    String group = "";
    String key = "";

    switch (account.getRole()) {
      case KLANT -> {
        group = account.getCustomerCode();
        key = "SECRET-KEY-" + account.getCustomerCode();
      }
      case BEHEERDER -> {
        group = apiSecretAdminGroup;
        key = apiSecretAdminKey;
      }
    }

    String json = "{\"group\": \"" + group + "\",\"apiKey\": \"" + key + "\"}";

    HttpRequest req = HttpRequest.newBuilder()
        .setHeader("Content-Type", "application/json")
        .uri(URI.create(BASE_URL + "/authenticate/token"))
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    CompletableFuture<HttpResponse<String>> futres = http.sendAsync(req, HttpResponse.BodyHandlers.ofString());

    try {
      HttpResponse<String> res = futres.get();
      if (res != null && res.body() != null) {
        Map<String, Object> resMap = mapper.readValue(res.body(), new TypeReference<Map<String, Object>>() {
        });
        if (resMap != null) {
          String token = (String) resMap.get("token");
          if (token != null) {
            switch (account.getRole()) {
              case KLANT -> {
                bearerTokens.put(account.getCustomerCode(), new BearerToken(token));
                return bearerToken;
              }
              case BEHEERDER -> {
                adminBearerToken.setToken(token);
                return adminBearerToken;
              }
            }
          }
        }
      }
    } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
      sentryExceptionHelper.warn(e);
      Thread.currentThread().interrupt();
    }
    return null;
  }
  // #endregion
}
