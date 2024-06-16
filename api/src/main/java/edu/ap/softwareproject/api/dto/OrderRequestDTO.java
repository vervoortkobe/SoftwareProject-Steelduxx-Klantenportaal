package edu.ap.softwareproject.api.dto;

import java.util.List;

public record OrderRequestDTO(
    String customerCode,
    String transportType,
    String customerReferenceNumber,
    String portCode,
    String cargoType,
    List<OrderDetailsProductDTO> products
) {
}