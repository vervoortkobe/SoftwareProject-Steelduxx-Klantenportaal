package edu.ap.softwareproject.api.dto;

import java.util.List;

public record OrderDetailsDTO(
    String referenceNumber,
    String customerReferenceNumber,
    String state,
    String transportType,
    String portOfOriginCode,
    String portOfOriginName,
    String portOfDestinationCode,
    String portOfDestinationName,
    String shipName,
    String shipIMO,
    String shipMMSI,
    String shipType,
    String ets,
    String ats,
    String eta,
    String ata,
    String preCarriage,
    String estimatedTimeCargoOnQuay,
    String actualTimeCargoLoaded,
    String billOfLadingDownloadLink,
    String packingListDownloadLink,
    String customsDownloadLink,
    String cargoType,
    List<OrderDetailsProductDTO> products) {
}