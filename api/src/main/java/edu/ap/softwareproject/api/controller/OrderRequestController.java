package edu.ap.softwareproject.api.controller;

import edu.ap.softwareproject.api.dto.OrderRequestDTO;
import edu.ap.softwareproject.api.entity.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import edu.ap.softwareproject.api.service.AccountService;
import edu.ap.softwareproject.api.service.OrderRequestService;
import edu.ap.softwareproject.api.service.ShipmentAPIService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/requests")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderRequestController {
  private final OrderRequestService orderRequestService;
  private final AccountService accountService;

  public OrderRequestController(OrderRequestService orderRequestService,
      AccountService accountService) {
    this.orderRequestService = orderRequestService;
    this.accountService = accountService;
  }
  @Operation(summary = "Gets all order requests.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "A list of order requests."),
  })
  @GetMapping
  public ResponseEntity<Optional<List<OrderRequest>>> getAllOrderRequests(HttpServletRequest request) {
    Optional<List<OrderRequest>> orderRequests = orderRequestService.getAllOrderRequests(
        accountService.getAccountFromRequest(request));
    return ResponseEntity.ok(orderRequests);
  }

  @Operation(summary = "Gets an order request by id.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "The order request."),
          @ApiResponse(responseCode = "404", description = "The order request does not exist.", content = @Content(schema = @Schema(implementation = Void.class))),
  })
  @GetMapping("{id}")
  public ResponseEntity<OrderRequest> getOrderRequestById(HttpServletRequest request,
      @PathVariable("id") String id) {
    Optional<OrderRequest> orderRequest = orderRequestService
        .findOrderRequestById(accountService.getAccountFromRequest(request), id);
    return orderRequest.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "Creates a new order request.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "The created order request."),
          @ApiResponse(responseCode = "401", description = "The user is forbidden to create an order request.", content = @Content(schema = @Schema(implementation = Void.class))),
  })
  @PostMapping
  public ResponseEntity<OrderRequest> createNewOrderRequest(HttpServletRequest request,
      @RequestBody OrderRequestDTO orderRequest) {
    Optional<OrderRequest> savedOrderRequest = orderRequestService
        .createNewOrderRequest(accountService.getAccountFromRequest(request), orderRequest);
    return savedOrderRequest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(401).build());
  }

  @Operation(summary = "Edits an order request")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "The edited order request."),
          @ApiResponse(responseCode = "404", description = "The order request does not exist", content = @Content(schema = @Schema(implementation = Void.class))),
  })
  @PatchMapping("{id}")
  public ResponseEntity<OrderRequest> update(HttpServletRequest request,
          @RequestBody OrderRequestDTO orderRequest,
          @PathVariable("id") String id) {
    // We gebruiken de shipmentRepository hier ipv de orderRequestService, omdat we
    // nog extra methods willen uitvoeren.

    Boolean success = ShipmentAPIService
        .overwriteOrderRequest(id, orderRequest, accountService.getAccountFromRequest(request));

    return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }
}