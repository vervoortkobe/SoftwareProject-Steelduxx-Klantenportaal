package edu.ap.softwareproject.api.controller;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.ap.softwareproject.api.dto.AdminOrderDTO;
import edu.ap.softwareproject.api.dto.OrderDetailsDTO;
import edu.ap.softwareproject.api.service.AccountService;
import edu.ap.softwareproject.api.service.AdminShipmentService;
import edu.ap.softwareproject.api.service.ShipmentAPIService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminOverviewController {
  private final AdminShipmentService adminShipmentService;
  private final AccountService accountService;
  private final ShipmentAPIService shipmentRepository;

  public AdminOverviewController(AdminShipmentService adminShipmentService, AccountService accountService,
      ShipmentAPIService shipmentRepository) {
    this.adminShipmentService = adminShipmentService;
    this.accountService = accountService;
    this.shipmentRepository = shipmentRepository;
  }

  @Operation(summary = "Gets all company codes.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "A list of company codes."),
          @ApiResponse(responseCode = "401", description = "Not authenticated or an administrator.", content = @Content(schema = @Schema(implementation = Void.class)))
  })
  @GetMapping("/codes")
  public Optional<List<String>> codes(HttpServletRequest request) {
    return adminShipmentService.getAllCodes(accountService.getAccountFromRequest(request));
  }

  @Operation(summary = "Gets all orders for an admin.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "A list of orders."),
          @ApiResponse(responseCode = "401", description = "Not authenticated or an administrator.", content = @Content(schema = @Schema(implementation = Void.class)))
  })
  @GetMapping("/orders")
  public Optional<List<AdminOrderDTO>> getAdminorders(HttpServletRequest request) {
    return adminShipmentService.getAll(accountService.getAccountFromRequest(request));
  }

  @Operation(summary = "Gets the detail of an order.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "The order detail."),
          @ApiResponse(responseCode = "404", description = "The order detail does not exist.", content = @Content(schema = @Schema(implementation = Void.class))),
          @ApiResponse(responseCode = "401", description = "Not authenticated or an administrator.", content = @Content(schema = @Schema(implementation = Void.class)))
  })
  @GetMapping("/orders/{group}/{id}")
  public ResponseEntity<OrderDetailsDTO> getAdminorderDetails(HttpServletRequest request, @PathVariable("group") String group,
      @PathVariable("id") String id) {
    Optional<OrderDetailsDTO> orderDetails = adminShipmentService.getById(accountService.getAccountFromRequest(request),
        group, id);
    return orderDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Approves an order request and sends it to the API.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "The order request has been approved."),
          @ApiResponse(responseCode = "404", description = "The order request does not exist.", content = @Content(schema = @Schema(implementation = Void.class))),
          @ApiResponse(responseCode = "401", description = "Not authenticated or an administrator.", content = @Content(schema = @Schema(implementation = Void.class)))
  })
  @PostMapping("/requests/{id}")
  public ResponseEntity<OrderDetailsDTO> approveOrderRequest(HttpServletRequest request, @PathVariable("id") String id) {
    Optional<OrderDetailsDTO> savedOrderRequest = shipmentRepository
        .approveOrderRequest(accountService.getAccountFromRequest(request), id);
    return savedOrderRequest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Denies and deletes an order request.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "The order request has been approved."),
          @ApiResponse(responseCode = "404", description = "The order request does not exist."),
          @ApiResponse(responseCode = "401", description = "Not authenticated or an administrator.", content = @Content(schema = @Schema(implementation = Void.class)))
  })
  @DeleteMapping("/requests/{id}")
  public ResponseEntity<Void> denyOrderRequest(HttpServletRequest request, @PathVariable("id") String id) {
    Boolean success = shipmentRepository.denyOrderRequest(accountService.getAccountFromRequest(request), id);
    return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }
}
