package edu.ap.softwareproject.api.service;

import edu.ap.softwareproject.api.dto.OrderRequestDTO;
import edu.ap.softwareproject.api.dto.OrderRequestMapper;
import edu.ap.softwareproject.api.entity.OrderRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.repository.OrderRequestRepository;
import jakarta.transaction.Transactional;

/**
 * The service for approval, editing and creation of order requests from a client.
 */
@Service
public class OrderRequestService {
  private final OrderRequestRepository orderRequestRepository;
  private final OrderRequestMapper orderRequestMapper = new OrderRequestMapper();
  private final AdminShipmentAPIService adminShipmentAPIService;
  public OrderRequestService(OrderRequestRepository orderRequestRepository, AdminShipmentAPIService adminShipmentAPIService) {
    this.orderRequestRepository = orderRequestRepository;
    this.adminShipmentAPIService = adminShipmentAPIService;
  }

  /**
   * Finds an order request by Id.
   * @param accountOptional The account making the request.
   * @param id The id of the order.
   * @return An order request object.
   */
  public Optional<OrderRequest> findOrderRequestById(Optional<Account> accountOptional, String id) {
    Optional<OrderRequest> orderRequest = orderRequestRepository.findById(id);
    if (orderRequest.isPresent() && accountOptional.isPresent())
      if (!orderRequest.get().getCustomerCode().equals(accountOptional.get().getCustomerCode()) && !accountOptional.get().isAdmin())
        return Optional.empty();
    return orderRequest;
  }

  /**
   * Gets all order requests for a given account.
   * @param accountOptional The account making the request.
   * @return A list of order requests.
   */
  public Optional<List<OrderRequest>> getAllOrderRequests(Optional<Account> accountOptional) {
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      if (account.isAdmin()) {
        Optional<Iterable<OrderRequest>> optionalIterable = Optional.of(orderRequestRepository.findAll());
        return optionalIterable.map(OrderRequestService::iterableToList);
      }
      return orderRequestRepository.findByCustomerCode(account.getCustomerCode());
    }
    return Optional.empty();
  }

  public Optional<List<OrderRequest>> findByCustomerCode(String customerCode) {
    return orderRequestRepository.findByCustomerCode(customerCode);
  }

  public Optional<OrderRequest> findByCustomerReferenceNumber(String customerReferenceNumber) {
    return orderRequestRepository.findByCustomerReferenceNumber(customerReferenceNumber);
  }

  /**
   * Creates a new order request for review by an admin.
   * @param accountOptional The account making the request.
   * @param orderRequestDTO The DTO object sent from the frontend.
   * @return An order request.
   */
  public Optional<OrderRequest> createNewOrderRequest(Optional<Account> accountOptional, OrderRequestDTO orderRequestDTO) {

    if (accountOptional.isEmpty())
      return Optional.empty();

    if (!accountOptional.get().isAdmin() && !orderRequestDTO.customerCode().equals(accountOptional.get().getCustomerCode()))
      return Optional.empty();

    Optional<List<String>> codes = adminShipmentAPIService.getAllCodes();

    if (!codes.get().contains(orderRequestDTO.customerCode()))
      return Optional.empty();

    // Check if order with customer key doesn't already exist:
    Optional<OrderRequest> request = orderRequestRepository.findByCustomerReferenceNumber(orderRequestDTO.customerReferenceNumber());

    if (request.isPresent())
      return Optional.empty();

    OrderRequest orderRequest = orderRequestMapper.apply(orderRequestDTO);
    return Optional.of(orderRequestRepository.save(orderRequest));
  }

  public Optional<OrderRequest> createNewOrderRequest(OrderRequest orderRequest) {
    return Optional.of(orderRequestRepository.save(orderRequest));
  }

  @Transactional
  public void deleteById(String id) {
    orderRequestRepository.deleteById(id);
  }

  private static <T> List<T> iterableToList(Iterable<T> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false).toList();
  }
}
