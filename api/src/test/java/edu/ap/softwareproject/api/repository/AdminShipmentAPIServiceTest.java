package edu.ap.softwareproject.api.repository;

import edu.ap.softwareproject.api.ShipmentRepositoryObjectMother;
import edu.ap.softwareproject.api.dto.AdminOrderDTO;
import edu.ap.softwareproject.api.enums.Role;
import org.junit.jupiter.api.Test;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AdminShipmentAPIServiceTest {
  @Test
  void testGetAllCodes() {
    String BASE_URL = "https://sw11-1.devops-ap.be/order";

    // Mock HttpResponse
    var mockResponse = mock(HttpResponse.class);

    // Configureer het mockResponse object
    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(ShipmentRepositoryObjectMother
        .parseToList("[\"SOF1\",\"SOF2\",\"SOF3\",\"SOF4\",\"SOF5\",\"SOF6\",\"SOF7\",\"SOF8\",\"SOF9\",\"SOF10\"]"));

    // Configureer de createGetReq & execGetReq methods
    HttpRequest req = ShipmentRepositoryObjectMother.createGetReq(Role.BEHEERDER, BASE_URL + "/codes", "mockToken");
    @SuppressWarnings("unchecked")
    HttpResponse<List<String>> res = ShipmentRepositoryObjectMother.execGetReq(req, mockResponse);

    // Gebruik de instantie van de ShipmentRepositoryObjectMother als repository
    ShipmentRepositoryObjectMother repository = ShipmentRepositoryObjectMother.getRepo();

    // Voer de methode uit die we willen testen
    Optional<List<String>> codes = repository.getAllCodes(Optional.of(res.body()));

    // Verifieer het resultaat
    assertTrue(codes.isPresent()); // controleren of de Optional een waarde bevat
  }

  @Test
  void testGetAllOrders() {
    String BASE_URL = "https://sw11-1.devops-ap.be/order";

    // Mock HttpResponse
    var mockResponse = mock(HttpResponse.class);

    // Configureer het mockResponse object
    when(mockResponse.statusCode()).thenReturn(200);
    when(mockResponse.body()).thenReturn(ShipmentRepositoryObjectMother.parseJsonToList(
        "[{\"customerCode\":\"SOF1\",\"referenceNumber\":\"2288312000\",\"customerReferenceNumber\":\"qC6JhJz7Gf2fCbkiAqYi\",\"state\":\"CLOSED\",\"transportType\":\"IMPORT\",\"portOfOriginCode\":\"NLROT\",\"portOfOriginName\":\"Rotterdam, Netherlands\",\"portOfDestinationCode\":\"BEANR\",\"portOfDestinationName\":\"Antwerp, Belgium\",\"shipName\":\"MSC TAMPA\",\"shipMMSI\":\"636021760\",\"ets\":\"26-03-2024 16:08\",\"ats\":\"26-03-2024 13:08\",\"eta\":\"18-04-2024 16:08\",\"ata\":\"19-04-2024 00:08\",\"cargoType\":\"BULK\",\"totalWeight\":3399000,\"totalContainers\":0,\"containerTypes\":[]}]"));

    // Configureer de createGetReq & execGetStringReq methods
    HttpRequest req = ShipmentRepositoryObjectMother.createGetReq(Role.BEHEERDER, BASE_URL + "/orders", "mockToken");
    @SuppressWarnings("unchecked")
    HttpResponse<List<AdminOrderDTO>> res = ShipmentRepositoryObjectMother.execGetMapStringReq(req, mockResponse);

    // Gebruik de instantie van de ShipmentRepositoryObjectMother als repository
    ShipmentRepositoryObjectMother repository = ShipmentRepositoryObjectMother.getRepo();

    // Voer de methode uit die we willen testen
    Optional<List<AdminOrderDTO>> orders = repository.getAllOrders(Optional.of(res.body()));

    // Verifieer het resultaat
    assertNotNull(orders); // controleren of de Optional niet null is
    assertTrue(orders.isPresent()); // controleren of de Optional een waarde bevat

    List<AdminOrderDTO> orderList = orders.get();
    assertEquals(1, orderList.size()); // controleren of de lijst één element bevat

    AdminOrderDTO order = orderList.get(0);
    assertEquals("SOF1", order.customerCode()); // controleren of de customer code overeenkomt
    assertEquals("2288312000", order.referenceNumber()); // controleren of de reference number overeenkomt
  }
}