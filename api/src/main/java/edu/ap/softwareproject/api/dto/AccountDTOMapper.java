package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.Account;

import java.util.function.Function;

public class AccountDTOMapper implements Function<Account, AccountDTO> {
  @Override
  public AccountDTO apply(Account account) {
    return new AccountDTO(
        account.getId(),
        account.getEmail(),
        account.getRole().ordinal(),
        account.getCustomerCode(),
        account.getAccount_information());
  }
}
