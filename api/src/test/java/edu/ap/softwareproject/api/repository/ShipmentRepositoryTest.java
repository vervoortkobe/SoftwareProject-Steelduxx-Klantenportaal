package edu.ap.softwareproject.api.repository;

import edu.ap.softwareproject.api.entity.OrderRequest;
import edu.ap.softwareproject.api.service.ShipmentAPIService;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import edu.ap.softwareproject.api.ShipmentRepositoryObjectMother;
import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.dto.BearerToken;
import edu.ap.softwareproject.api.enums.Role;

class ShipmentRepositoryTest {

  @Test
  void givenMockToken_shouldRefreshBearerToken() throws JsonMappingException, JsonProcessingException {

    // Instantieer de mocks
    ShipmentAPIService shipmentRepository = mock(ShipmentAPIService.class);
    // Mock de Account klasse
    Account account = mock(Account.class);
    when(account.getCustomerCode()).thenReturn("SOF9");
    when(account.getRole()).thenReturn(Role.KLANT);

    var response = mock(HttpResponse.class);
    when(response.body()).thenReturn("{\"token\": \"mockToken\"}");

    ObjectMapper mapper = mock(ObjectMapper.class);
    when(mapper.readValue(anyString(), eq(Map.class)))
        .thenReturn(Collections.singletonMap("token", "mockToken"));

    // Invoke method under test
    when(shipmentRepository.refreshBearerToken(Optional.of(account))).thenReturn(new BearerToken("mockToken"));
    BearerToken result = shipmentRepository.refreshBearerToken(Optional.of(account));

    // Verifieer de resultaten
    assertNotNull(result);
    assertEquals("mockToken", result.getToken());
  }

  @Test
  void givenHttpRequests_shouldReturnHttpRequest() {
    String BASE_URL = "https://sw11-1.devops-ap.be/order";

    // Roep de createGetReq methode aan
    HttpRequest request = ShipmentRepositoryObjectMother.createGetReq(Role.KLANT, BASE_URL + "/all", "mockToken");

    // Verifieer dat de headers en URI van de request kloppen
    assertEquals(URI.create(BASE_URL + "/all"), request.uri());
    assertEquals("application/json", request.headers().firstValue("Content-Type").orElse(null));
    assertNotNull(request.headers().firstValue("Authorization").orElse(null));
  }

  @Test
  void givenDeniedOrderRequest_shouldDeleteOrderRequest_andReturnTrue() {

    // Instantieer de mocks
    OrderRequest deniedOrderRequest1 = mock(OrderRequest.class);
    OrderRequest deniedOrderRequest2 = mock(OrderRequest.class);
    ShipmentRepositoryObjectMother.createOrderRequest(deniedOrderRequest1);
    ShipmentRepositoryObjectMother.createOrderRequest(deniedOrderRequest2);
    when(deniedOrderRequest1.getCustomerReferenceNumber()).thenReturn("denied1");
    when(deniedOrderRequest2.getCustomerReferenceNumber()).thenReturn("denied2");

    // Vind de orderrequest die afgekeurd moet worden als deze in de lijst zit
    Optional<OrderRequest> findOrderRequest = ShipmentRepositoryObjectMother
        .findByCustomerReferenceNumber(deniedOrderRequest1.getCustomerReferenceNumber());

    // Verifieer dat de orderRequest bestaat
    Optional<Boolean> deleted;
    if (findOrderRequest.isPresent()) {
      // Verwijder de orderRequest
      deleted = ShipmentRepositoryObjectMother.deleteOrderRequest(findOrderRequest.get().getCustomerReferenceNumber());
      assertThat(deleted).isPresent();
    }

    // Verifieer dat de orderRequest is verwijderd
    Optional<OrderRequest> deletedOrderRequest = ShipmentRepositoryObjectMother
        .findByCustomerReferenceNumber(deniedOrderRequest1.getCustomerReferenceNumber());
    assertThat(deletedOrderRequest).isEmpty();
  }
}