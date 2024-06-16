package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.AccountInformation;

import java.util.function.Function;

public class AccountInformationMapper implements Function<AccountInformationDTO, AccountInformation> {

    @Override
    public AccountInformation apply(AccountInformationDTO accountInformationDTO) {
        return new AccountInformation(
                accountInformationDTO.getCompany_name(),
                accountInformationDTO.getContact_firstname(),
                accountInformationDTO.getContact_lastname(),
                accountInformationDTO.getContact_telephone(),
                accountInformationDTO.getCountryId(),
                accountInformationDTO.getStreet(),
                accountInformationDTO.getCity(),
                accountInformationDTO.getPostal_code(),
                accountInformationDTO.getStreet_number(),
                accountInformationDTO.getBox(),
                accountInformationDTO.getStreet_secondary(),
                accountInformationDTO.getBtw_number()
        );
    }
}
