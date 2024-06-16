package edu.ap.softwareproject.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class AccountInformationDTO {
  private String company_name;
  private String contact_firstname;
  private String contact_lastname;
  private String contact_telephone;
  private Integer countryId;
  private String street;
  private String city;
  private short postal_code;
  private short street_number;
  private String box;
  private String street_secondary;
  private String btw_number;
}
