package edu.ap.softwareproject.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ap.softwareproject.api.dto.AdminOrderDTO;
import edu.ap.softwareproject.api.entity.OrderRequest;
import edu.ap.softwareproject.api.enums.Role;

import static edu.ap.softwareproject.api.ApiApplication.sentry;

public class ShipmentRepositoryObjectMother {

  private static final HttpClient http = HttpClient.newHttpClient();

  public static String getHeaderValue(HttpRequest request, String headerName) {
    HttpHeaders headers = request.headers();
    return headers.firstValue(headerName).orElse(null);
  }

  public static HttpRequest createGetReq(Role role, String url, String bearerToken) {
    if (url == null || url.isEmpty()) {
      throw new IllegalArgumentException("URL cannot be null or empty");
    }

    return HttpRequest.newBuilder()
        .setHeader("Authorization",
            "Bearer " + bearerToken)
        .setHeader("Content-Type", "application/json")
        .uri(URI.create(url))
        .GET()
        .build();
  }

  public static HttpResponse<List<String>> execGetReq(HttpRequest req, HttpResponse<List<String>> mockResponse) {
    try {
      http.sendAsync(req, HttpResponse.BodyHandlers.ofString()).get();
      return mockResponse;
    } catch (InterruptedException | ExecutionException e) {
      return null;
    }
  }

  public static HttpResponse<List<AdminOrderDTO>> execGetMapStringReq(HttpRequest req,
      HttpResponse<List<AdminOrderDTO>> mockResponse) {
    try {
      http.sendAsync(req, HttpResponse.BodyHandlers.ofString()).get();
      return mockResponse;
    } catch (InterruptedException | ExecutionException e) {
      return null;
    }
  }

  public static List<String> parseToList(String input) {
    List<String> list = new ArrayList<>();
    Pattern pattern = Pattern.compile("\"(.*?)\"");
    Matcher matcher = pattern.matcher(input);
    while (matcher.find()) {
      list.add(matcher.group(1));
    }
    return list;
  }

  public static List<AdminOrderDTO> parseJsonToList(String input) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(input,
          objectMapper.getTypeFactory().constructCollectionType(List.class, AdminOrderDTO.class));
    } catch (JsonProcessingException e) {
      sentry.warn(e);
      return new ArrayList<>();
    }
  }

  public static ShipmentRepositoryObjectMother getRepo() {
    return new ShipmentRepositoryObjectMother();
  }

  public Optional<List<String>> getAllCodes(Optional<List<String>> optional) {
    return optional.get() == null ? Optional.empty() : Optional.of(optional.get());
  }

  public Optional<List<AdminOrderDTO>> getAllOrders(Optional<List<AdminOrderDTO>> optional) {
    return optional.get() == null ? Optional.empty() : Optional.of(optional.get());
  }

  private static final List<OrderRequest> allOrderRequests = new ArrayList<>();

  public static Optional<List<OrderRequest>> getAllOrderRequests() {
    return Optional.of(allOrderRequests);
  }

  public static Optional<OrderRequest> createOrderRequest(OrderRequest orderRequest) {
    allOrderRequests.add(orderRequest);
    return Optional.of(orderRequest);
  }

  public static Optional<Boolean> deleteOrderRequest(String customerReferenceNumber) {
    allOrderRequests
        .removeIf(orderRequest -> Objects.equals(orderRequest.getCustomerReferenceNumber(), customerReferenceNumber));
    return Optional.of(true);
  }

  public static Optional<OrderRequest> findByCustomerReferenceNumber(String customerReferenceNumber) {
    return allOrderRequests.stream()
        .filter(orderRequest -> Objects.equals(orderRequest.getCustomerReferenceNumber(), customerReferenceNumber))
        .findFirst();
  }
}
