package edu.ap.softwareproject.api.controller;

import java.util.List;
import java.util.Optional;

import edu.ap.softwareproject.api.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ap.softwareproject.api.dto.OrderDTO;
import edu.ap.softwareproject.api.dto.OrderDetailsDTO;
import edu.ap.softwareproject.api.service.ShipmentService;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OverviewController {
  private final ShipmentService shipmentService;
  private final AccountService accountService;

  public OverviewController(ShipmentService shipmentService, AccountService accountService) {
    this.shipmentService = shipmentService;
    this.accountService = accountService;
  }

  @Operation(summary = "Gets all the orders of a customer.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "A list of orders."),
  })
  @GetMapping()
  public Optional<List<OrderDTO>> orders(HttpServletRequest request) {
    return shipmentService.getAll(accountService.getAccountFromRequest(request));
  }

  @Operation(summary = "Gets the detail of an order by id.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "The order detail."),
          @ApiResponse(responseCode = "404", description = "The order does not exist.", content = @Content(schema = @Schema(implementation = Void.class))),
  })
  @GetMapping("/{id}")
  public ResponseEntity<OrderDetailsDTO> orderDetails(HttpServletRequest request, @PathVariable("id") String id) {
    Optional<OrderDetailsDTO> orderDetails = shipmentService.getById(accountService.getAccountFromRequest(request), id);
    return orderDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}
