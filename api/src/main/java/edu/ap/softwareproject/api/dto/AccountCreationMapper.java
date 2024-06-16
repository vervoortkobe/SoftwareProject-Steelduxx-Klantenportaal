package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.enums.Role;
import edu.ap.softwareproject.api.security.SecurityConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Function;

//Used when creating an account.
public class AccountCreationMapper implements Function<AccountCreationDTO, Account> {
    @Override
    public Account apply(AccountCreationDTO accountCreationDTO) {
        PasswordEncoder encoder = SecurityConfiguration.passwordEncoder();

        return new Account(
                accountCreationDTO.getEmail(),
                encoder.encode(accountCreationDTO.getPassword()),
                false,
                Role.KLANT);
    }
}
