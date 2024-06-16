package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.AccountInformation;

public record AccountDTO(
        Long id,
        String email,
        Integer role,
        String customerCode,
        AccountInformation accountInformation) {
}