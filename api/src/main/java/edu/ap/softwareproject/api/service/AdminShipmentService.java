package edu.ap.softwareproject.api.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import edu.ap.softwareproject.api.dto.AdminOrderDTO;
import edu.ap.softwareproject.api.dto.OrderDetailsDTO;
import edu.ap.softwareproject.api.entity.Account;

/**
 * A service to get shipment information only a Steelduxx admin can see.
 */
@Service
public class AdminShipmentService {
  private final AdminShipmentAPIService adminShipmentAPIService;

  public AdminShipmentService(AdminShipmentAPIService adminShipmentAPIService) {
    this.adminShipmentAPIService = adminShipmentAPIService;
  }

  /**
   * Gets all the customer codes that an admin can see.
   * 
   * @param accountOptional The account making the request.
   * @return A list of customer codes.
   */
  public Optional<List<String>> getAllCodes(Optional<Account> accountOptional) {
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      return adminShipmentAPIService.getAllCodes();
    }
    return Optional.empty();
  }

  /**
   * Gets all the orders of an admin.
   * 
   * @param accountOptional The account making the request.
   * @return A DTO list of AdminOrders.
   */
  public Optional<List<AdminOrderDTO>> getAll(Optional<Account> accountOptional) {
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      return adminShipmentAPIService.getAllOrders(account);
    }
    return Optional.empty();
  }

  /**
   * Gets the detail of an order for use by an admin.
   * 
   * @param accountOptional The account making the request.
   * @param groupCode       The customer code of the order.
   * @param orderId         The order Id of the order.
   * @return An order detail DTO object.
   */
  public Optional<OrderDetailsDTO> getById(Optional<Account> accountOptional, String groupCode, String orderId) {
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      return adminShipmentAPIService.getOrderById(account, groupCode, orderId);
    }
    return Optional.empty();
  }
}
