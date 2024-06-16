package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.Account;

import java.util.function.Function;

public class AccountApprovalDTOMapper implements Function<Account, AccountApprovalDTO> {
  @Override
  public AccountApprovalDTO apply(Account account) {
    return new AccountApprovalDTO(
        account.getId(),
        account.getApproved(),
        account.getEmail(),
        account.getRole().ordinal(),
        account.getCustomerCode(),
        account.getAccount_information());
  }
}
