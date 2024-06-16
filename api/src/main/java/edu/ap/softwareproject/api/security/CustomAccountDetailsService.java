package edu.ap.softwareproject.api.security;

import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomAccountDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    private GrantedAuthority mapRoleToAuthority(String role) {
        return new SimpleGrantedAuthority("ROLE_" + role);
    }

    public CustomAccountDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account user = accountRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Account not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Collections.singleton(mapRoleToAuthority(user.getRole().name())));
    }
}
