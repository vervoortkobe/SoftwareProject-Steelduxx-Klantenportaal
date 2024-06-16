package edu.ap.softwareproject.api.service;

import edu.ap.softwareproject.api.enums.Role;
import org.springframework.stereotype.Service;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import io.jsonwebtoken.io.IOException;

import edu.ap.softwareproject.api.dto.AdminOrderDTO;
import edu.ap.softwareproject.api.dto.OrderDetailsDTO;
import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.util.ShipmentRepositoryUtil;

import static edu.ap.softwareproject.api.ApiApplication.sentry;

/**
 * An API-layer to make requests to the Steelduxx API for the admin.
 */
@Service
public class AdminShipmentAPIService {

  public AdminShipmentAPIService() {
  }

  // #region getAllCodes
  /*
   * A method to retrieve all codes using an HTTP GET request.
   *
   * @return List of String objects representing the retrieved codes
   */
  public Optional<List<String>> getAllCodes() {

    try {

      // :')
      Account account = new Account();
      account.setRole(Role.BEHEERDER);

      HttpRequest req = ShipmentRepositoryUtil
          .createGetReq(account, ShipmentRepositoryUtil.BASE_URL + "/admin/company-codes/all");
      HttpResponse<String> res = ShipmentRepositoryUtil.execReq(req);

      if (res.statusCode() == 401 || res.body() == null) {
        ShipmentRepositoryUtil.setAdminBearerToken(ShipmentRepositoryUtil.refreshBearerToken(account));
        req = ShipmentRepositoryUtil.createGetReq(account,
            ShipmentRepositoryUtil.BASE_URL + "/admin/company-codes/all");
        res = ShipmentRepositoryUtil.execReq(req);
      }

      return ShipmentRepositoryUtil.createResListMapper(account, res,
          String.class);
    } catch (IOException e) {
      sentry.warn(e);
      return Optional.empty();
    }
  }
  // #endregion

  // #region getAllOrders
  /**
   * A method to retrieve all orders using an HTTP GET request.
   *
   * @param account The account making the request.
   * @return List of OrderDTO objects representing the retrieved orders
   */
  public Optional<List<AdminOrderDTO>> getAllOrders(Account account) {

    HttpRequest req = ShipmentRepositoryUtil.createGetReq(account,
        ShipmentRepositoryUtil.BASE_URL + "/admin/order/all");
    try {
      HttpResponse<String> res = ShipmentRepositoryUtil.execReq(req);

      if (res.statusCode() == 401 || res.body() == null) {
        ShipmentRepositoryUtil.setAdminBearerToken(ShipmentRepositoryUtil.refreshBearerToken(account));
        req = ShipmentRepositoryUtil.createGetReq(account,
            ShipmentRepositoryUtil.BASE_URL + "/admin/order/all");
        res = ShipmentRepositoryUtil.execReq(req);
      }

      return ShipmentRepositoryUtil.createResListMapper(account, res,
          AdminOrderDTO.class);
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
   * @param account   The account making the request.
   * @param groupCode the group code of the order
   * @param orderId   the ID of the order
   * @return the order details DTO
   */
  public Optional<OrderDetailsDTO> getOrderById(Account account, String groupCode, String orderId) {

    HttpRequest req = ShipmentRepositoryUtil
        .createGetReq(account, ShipmentRepositoryUtil.BASE_URL + "/admin/order/" + groupCode + "/" + orderId);
    try {
      HttpResponse<String> res = ShipmentRepositoryUtil.execReq(req);

      if (res.statusCode() == 401 || res.body() == null) {
        ShipmentRepositoryUtil.setAdminBearerToken(ShipmentRepositoryUtil.refreshBearerToken(account));
        req = ShipmentRepositoryUtil
            .createGetReq(account,
                ShipmentRepositoryUtil.BASE_URL + "/admin/order/" + groupCode + "/" + orderId);
        res = ShipmentRepositoryUtil.execReq(req);
      }

      return ShipmentRepositoryUtil.createResMapper(account, res, OrderDetailsDTO.class);
    } catch (IOException e) {
      sentry.warn(e);
      return Optional.empty();
    }
  }
  // #endregion
}