package edu.ap.softwareproject.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;

import edu.ap.softwareproject.api.entity.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.enums.Role;
import edu.ap.softwareproject.api.repository.OrderRequestRepository;

class OrderRequestServiceTest {

  @Mock
  private OrderRequestRepository orderRequestRepository;
  @Mock
  private AdminShipmentAPIService adminShipmentAPIService;
  private OrderRequestService orderRequestService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    orderRequestService = new OrderRequestService(orderRequestRepository, adminShipmentAPIService);
  }

  @Test
  public void retrievesAllOrderRequests() {
    // Mock de Account klasse
    Account account = mock(Account.class);
    when(account.isAdmin()).thenReturn(true);
    when(account.getRole()).thenReturn(Role.KLANT);

    // Arrange
    List<OrderRequest> allOrderRequests = List.of(new OrderRequest(),
        new OrderRequest());
    when(orderRequestRepository.findAll()).thenReturn(allOrderRequests);

    // Act
    Optional<List<OrderRequest>> customerCodeOrderRequests = orderRequestService
        .getAllOrderRequests(Optional.of(account));
    List<OrderRequest> result = null;
    if (customerCodeOrderRequests.isPresent())
      result = customerCodeOrderRequests.get();

    // Assert
    assertThat(result).isEqualTo(allOrderRequests);
  }
}