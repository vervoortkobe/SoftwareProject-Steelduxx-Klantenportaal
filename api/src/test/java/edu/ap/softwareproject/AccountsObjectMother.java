package edu.ap.softwareproject;

import edu.ap.softwareproject.api.dto.AccountCreationDTO;
import edu.ap.softwareproject.api.dto.AccountInformationDTO;

public class AccountsObjectMother {
    public static AccountCreationDTO createMockValidUser() {
        return new AccountCreationDTO(
                "testuser@test.com",
                "VeryStr@ng#Password789",
                new AccountInformationDTO(
                        "Generico Inc.",
                        "John",
                        "Doe",
                        "3241245678",
                        1,
                        "Playgroundstret",
                        "Examplecity",
                        (short) 1,
                        (short) 11,
                        "22A",
                        "",
                        ""
                )
        );
    }
}
