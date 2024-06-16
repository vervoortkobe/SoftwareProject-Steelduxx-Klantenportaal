package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.AccountInformation;

public record AccountApprovalDTO (
        Long id,
        Boolean approved,
        String email,
        Integer role,
        String customerCode,
        AccountInformation accountInformation
) {
}
