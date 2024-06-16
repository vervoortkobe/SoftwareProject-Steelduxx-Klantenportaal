package edu.ap.softwareproject.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.ap.softwareproject.api.dto.OrderDTO;
import edu.ap.softwareproject.api.dto.OrderDetailsDTO;
import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.enums.Role;

class ShipmentServiceTest {

  @Mock
  private ShipmentAPIService shipmentRepository;

  private ShipmentService shipmentService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    shipmentService = new ShipmentService(shipmentRepository);
  }

  @Test
  void testGetAll() {
    // Mock de Account klasse
    Account account = mock(Account.class);
    when(account.getCustomerCode()).thenReturn("SOF9");
    when(account.getRole()).thenReturn(Role.KLANT);

    // Mock de respons van de repository
    Optional<List<OrderDTO>> expectedResponse = Optional.empty();
    when(shipmentRepository.getAllOrders(null)).thenReturn(expectedResponse);

    // Roep de methode getAll aan en verkrijg de daadwerkelijke respons
    Object actualResponse = shipmentService.getAll(Optional.of(new Account()));

    // Verifieer dat de repository-methode bestaat
    assertNotNull(shipmentRepository.getAllOrders(account));

    // Verifieer dat de daadwerkelijke respons overeenkomt met de verwachte respons
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  void givenOrderData_shouldReturnOrderDataCorrectly() {
    // Mock de Account klasse
    Account account = mock(Account.class);
    when(account.getCustomerCode()).thenReturn("SOF9");
    when(account.getRole()).thenReturn(Role.KLANT);

    // Mock de respons van de repository
    Optional<List<OrderDTO>> expectedResponse = Optional.of(new ArrayList<>()); // Dit moet overeenkomen met de
    // verwachte responsstructuur
    when(shipmentRepository.getAllOrders(any())).thenReturn(expectedResponse);

    // Roep de methode getAll aan en verkrijg de daadwerkelijke respons
    Object actualResponse = shipmentService.getAll(Optional.of(new Account()));

    // Verifieer dat de repository-methode bestaat
    assertNotNull(shipmentRepository.getAllOrders(account));

    // Verifieer dat de daadwerkelijke respons overeenkomt met de verwachte respons
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void givenOrderDetailsData_shouldReturnOrderDetailsDataCorrectly() {
    // Mock de Account klasse
    Account account = mock(Account.class);
    when(account.getCustomerCode()).thenReturn("SOF9");
    when(account.getRole()).thenReturn(Role.KLANT);

    // Declareer de respons van de repository
    Optional<OrderDetailsDTO> expectedResponse = Optional.of(
        new OrderDetailsDTO(
            "RefNr",
            "CustRefNr",
            "State",
            "TranspType",
            "POOCode",
            "POOName",
            "PODCode",
            "PODName",
            "ShipName",
            "ShipIMO",
            "ShipMMSI",
            "ShipType",
            "ETS",
            "ATS",
            "ETA",
            "ATA",
            "CONTAINER",
            "Precar",
            "ETCOQuay",
            "ATCLoaded",
            "BLDownload",
            "PLDLink",
            "CDLink",
            null)); // Dit moet overeenkomen met de verwachte responsstructuur
    String orderId = "1";
    when(shipmentRepository.getOrderById(account, orderId))
        .thenReturn(expectedResponse);

    // Roep de methode getAll aan en verkrijg de daadwerkelijke respons
    Optional<OrderDetailsDTO> actualResponse = shipmentService.getById(
        Optional.of(account),
        orderId);

    // Verifieer dat de repository-methode correct is aangeroepen
    verify(shipmentRepository, times(1)).getOrderById(account, orderId);

    // Verifieer dat de daadwerkelijke respons overeenkomt met de verwachte respons
    assertEquals(expectedResponse, actualResponse);
  }
}
