package edu.ap.softwareproject.api.service;

import edu.ap.softwareproject.api.dto.*;
import edu.ap.softwareproject.api.entity.*;
import edu.ap.softwareproject.api.enums.Role;
import org.springframework.stereotype.Service;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.jsonwebtoken.io.IOException;

import edu.ap.softwareproject.api.util.ShipmentRepositoryUtil;

import static edu.ap.softwareproject.api.ApiApplication.sentry;

/**
 * An API-layer to make requests to the Steelduxx API for a customer.
 */
@Service
public class ShipmentAPIService {
  private static OrderRequestService orderRequestService;
  private static AdminShipmentAPIService adminShipmentAPIService;

  public ShipmentAPIService(OrderRequestService orderRequestService, AdminShipmentAPIService adminShipmentAPIService) {
    ShipmentAPIService.orderRequestService = orderRequestService;
    this.adminShipmentAPIService = adminShipmentAPIService;
  }

  // #region getAllOrders
  /**
   * A method to retrieve all orders using an HTTP GET request.
   *
   * @return List of OrderDTO objects representing the retrieved orders
   */
  public Optional<List<OrderDTO>> getAllOrders(Account account) {

    HttpRequest req = ShipmentRepositoryUtil.createGetReq(account, ShipmentRepositoryUtil.BASE_URL + "/order/all");
    try {
      HttpResponse<String> res = ShipmentRepositoryUtil.execReq(req);

      if (res.statusCode() == 401 || res.body() == null) {
        ShipmentRepositoryUtil.setBearerToken(ShipmentRepositoryUtil.refreshBearerToken(account));
        req = ShipmentRepositoryUtil.createGetReq(account, ShipmentRepositoryUtil.BASE_URL + "/order/all");
        res = ShipmentRepositoryUtil.execReq(req);
      }

      return ShipmentRepositoryUtil.createResListMapper(account, res, OrderDTO.class);
    } catch (IOException e) {
      sentry.warn(e);
      return Optional.empty();
    }
  }
  // #endregion

  // #region getOrderById
  /**
   * Get order details by ID.
   *
   * @param orderId the ID of the order
   * @return the order details DTO
   */
  public Optional<OrderDetailsDTO> getOrderById(Account account, String orderId) {

    HttpRequest req = ShipmentRepositoryUtil.createGetReq(account,
        ShipmentRepositoryUtil.BASE_URL + "/order/" + orderId);
    try {
      HttpResponse<String> res = ShipmentRepositoryUtil.execReq(req);

      if (res.statusCode() == 401 || res.body() == null) {
        ShipmentRepositoryUtil.setBearerToken(ShipmentRepositoryUtil.refreshBearerToken(account));
        req = ShipmentRepositoryUtil.createGetReq(account, ShipmentRepositoryUtil.BASE_URL + "/order/" + orderId);
        res = ShipmentRepositoryUtil.execReq(req);
      }

      return ShipmentRepositoryUtil.createResMapper(account, res, OrderDetailsDTO.class);
    } catch (IOException e) {
      sentry.warn(e);
      return Optional.empty();
    }
  }

  // #endregion

  // #region approveOrderRequest

  /**
   * Approves and sends an order request to the Steelduxx API.
   * @param accountOptional The account which the request is requesting from
   * @param Id The id of the order request.
   * @return The result from the Steelduxx API.
   */
  public Optional<OrderDetailsDTO> approveOrderRequest(Optional<Account> accountOptional,
      String Id) {

    if (accountOptional.isPresent()) {
      Optional<OrderRequest> findOrderRequest = orderRequestService
          .findOrderRequestById(accountOptional, Id);

      if (findOrderRequest.isEmpty())
        return Optional.empty();

      // :')
      Account account = new Account();
      account.setCustomerCode(findOrderRequest.get().getCustomerCode());
      account.setRole(Role.KLANT);

      OrderRequestDTOMapper mapper = new OrderRequestDTOMapper();

      HttpRequest req = ShipmentRepositoryUtil.createPostReq(account,
          ShipmentRepositoryUtil.BASE_URL + "/order/new", mapper.apply(findOrderRequest.get()));
      try {
        HttpResponse<String> res = ShipmentRepositoryUtil.execReq(req);

        if (res.statusCode() == 401 || res.body() == null) {
          ShipmentRepositoryUtil.setBearerToken(
              ShipmentRepositoryUtil.refreshBearerToken(account));
          req = ShipmentRepositoryUtil.createGetReq(account, ShipmentRepositoryUtil.BASE_URL + "/order/new");
          res = ShipmentRepositoryUtil.execReq(req);
        }

        Optional<OrderDetailsDTO> result = ShipmentRepositoryUtil.createResMapper(account, res,
            OrderDetailsDTO.class);

        // If execution succeeds, delete the order request repository
        if (result.isPresent() && result.get() != null)
          orderRequestService.deleteById(findOrderRequest.get().getId());

        return result;
      } catch (IOException e) {
        sentry.warn(e);
        return Optional.empty();
      }
    }
    return Optional.empty();
  }
  // #endregion

  // #region denyOrderRequest

  /**
   * Denies an order request.
   * @param account The account that denies the order request.
   * @param Id The Id of the order request.
   */
  public boolean denyOrderRequest(Optional<Account> account, String Id) {
    if (!account.isPresent() || !account.get().isAdmin())
      return false;

    Optional<OrderRequest> findOrderRequest = orderRequestService
        .findOrderRequestById(account, Id);
    if (findOrderRequest.isEmpty())
      return false;

    orderRequestService.deleteById(Id);
    return true;
  }
  // #endregion

  // #region overwriteOrderRequest

  /**
   * Edits an existing order request.
   * @param id The id of the order request that needs updating.
   * @param updatedOrderRequest The updated information of the order request.
   * @param accountOptional The account making the request.
   * @return If the operation succeeded successfully.
   */
  public static Boolean overwriteOrderRequest(String id, OrderRequestDTO updatedOrderRequest,
                                                             Optional<Account> accountOptional) {
    Optional<OrderRequest> findOrderRequest = orderRequestService
        .findOrderRequestById(accountOptional, id);

    if (findOrderRequest.isPresent()) {
      OrderRequest existing = findOrderRequest.get();

      if (accountOptional.isPresent()) {
        Account account = accountOptional.get();

        if (account.isAdmin())
          existing.setCustomerCode(updatedOrderRequest.customerCode());

        Optional<List<String>> validCodes = adminShipmentAPIService.getAllCodes();

        if (!validCodes.get().contains(updatedOrderRequest.customerCode()))
          return false;

        if (!existing.getCustomerCode().equals(account.getCustomerCode()) && !account.isAdmin())
          return false;

        OrderRequestProductMapper mapper = new OrderRequestProductMapper();

        existing.setCustomerReferenceNumber(updatedOrderRequest.customerReferenceNumber());
        existing.setTransportType(updatedOrderRequest.transportType());
        existing.setPortCode(updatedOrderRequest.portCode());
        existing.setCargoType(updatedOrderRequest.cargoType());

        //Hibernate forced me to do this terribleness. I'm sorry.

        //The client will send us a list of products, with their id's
        //We will do an extra check to see if the id's that are supplied are actually valid!
        //If not, that means, that is a new product.
        //If one is missing, that means remove it.

        List<OrderRequestProduct> copy = new ArrayList<OrderRequestProduct>(existing.getProducts());
        copy.stream().forEach(
                (orderRequestProduct) -> {
                  Boolean exists = mapper.applyList(updatedOrderRequest.products()).contains(orderRequestProduct);
                  if (!exists)
                    existing.getProducts().remove(orderRequestProduct);
                }
        );

        updatedOrderRequest.products().forEach((product) -> {
          Optional<OrderRequestProduct> requestProduct = existing.getProducts().stream()
                  .filter((existingProduct) -> existingProduct.getId() != null) //WTF?
                  .filter((existingProduct) -> existingProduct.getId().equals(product.getId())).findFirst();

          if (requestProduct.isPresent()) {
            int index = existing.getProducts().indexOf(requestProduct.get());
            existing.getProducts().set(index, mapper.apply(product));
          } else {
            product.setId(null);
            existing.getProducts().add(mapper.apply(product));
          }
        });

        // Overwrite existing by saving it again using createNewOrderRequest
        orderRequestService.createNewOrderRequest(existing);
        return true;
      }
    }
    return false;
  }
  // #endregion

  // #region refreshBearerToken

  /**
   * Refreshes the bearertoken from the Steelduxx API for a given account.
   * @param accountOptional The account which to refresh the bearertoken for.
   * @return The refreshed bearertoken.
   */
  public BearerToken refreshBearerToken(Optional<Account> accountOptional) {
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      return ShipmentRepositoryUtil.refreshBearerToken(account);
    }
    return null;
  }
  // #endregion
}