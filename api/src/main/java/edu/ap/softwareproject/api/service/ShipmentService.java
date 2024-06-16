package edu.ap.softwareproject.api.service;

import java.util.List;
import java.util.Optional;
import edu.ap.softwareproject.api.entity.Account;
import org.springframework.stereotype.Service;
import edu.ap.softwareproject.api.dto.OrderDTO;
import edu.ap.softwareproject.api.dto.OrderDetailsDTO;

/**
 * A service for the non-admins to view their shipments.
 */
@Service
public class ShipmentService {
  private final ShipmentAPIService shipmentRepository;

  public ShipmentService(ShipmentAPIService shipmentRepository) {
    this.shipmentRepository = shipmentRepository;
  }

  /**
   * Gets all the orders for a user.
   * @param accountOptional The account of the user requesting their shipments.
   * @return All orders based on their customerCode or Role.
   */
  public Optional<List<OrderDTO>> getAll(Optional<Account> accountOptional) {
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      return shipmentRepository.getAllOrders(account);
    }
    return Optional.empty();
  }

  /**
   * Gets an order by Id.
   * @param accountOptional The account of the user requesting their shipments.
   * @param orderId The orderId requested by the user.
   * @return If the user has access to the order, the order will be returned.
   */
  public Optional<OrderDetailsDTO> getById(Optional<Account> accountOptional, String orderId) {
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      return shipmentRepository.getOrderById(account, orderId);
    }
    return Optional.empty();
  }
}
