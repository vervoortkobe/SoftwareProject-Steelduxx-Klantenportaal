package edu.ap.softwareproject.api.dto;

import java.util.List;

public record OrderDTO(
    String referenceNumber,
    String customerReferenceNumber,
    String state,
    String transportType,
    String portOfOriginCode,
    String portOfOriginName,
    String portOfDestinationCode,
    String portOfDestinationName,
    String shipName,
    String ets,
    String ats,
    String eta,
    String ata,
    String cargoType,
    int totalWeight,
    int totalContainers,
    List<String> containerTypes,
    String shipMMSI) {
}
