package edu.ap.softwareproject.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreationDTO {
  private String email;
  private String password;
  private AccountInformationDTO accountInformation;
}
