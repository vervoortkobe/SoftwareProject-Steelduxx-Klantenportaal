package edu.ap.softwareproject.api.service;

import edu.ap.softwareproject.AccountsObjectMother;
import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.exceptions.RegisterException;
import edu.ap.softwareproject.api.repository.AccountRepository;
import edu.ap.softwareproject.api.repository.AccountInformationRepository;
import edu.ap.softwareproject.api.security.JWTGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountInformationRepository accountInformationRepository;
    @Mock
    private CountryService countryService;
    @Mock
    private AdminShipmentService adminShipmentService;
    @Mock
    private AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator = new JWTGenerator();
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService(accountRepository, accountInformationRepository, countryService,
                jwtGenerator, adminShipmentService, authenticationManager);
    }

    @Test
    void givenCorrectAccount_ShouldBeCorrectlyValidated() {
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        when(countryService.countryExistsById(any(long.class))).thenReturn(true);

        Account mockAccount = new Account();
        mockAccount.setId(1L);

        when(accountRepository.save(any())).thenReturn(mockAccount);

        // Test should succeed with no problems.
        try {
            accountService.registerUser(AccountsObjectMother.createMockValidUser());
        } catch (RegisterException e) {
            System.out.println(e.reason);
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void givenAdminAccount_ShouldNotBeAllowed() {
        Account mockAdminAccount = new Account();
        mockAdminAccount.setId(-1L);

        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAdminAccount));

        boolean success = accountService.deleteAccount(-1L, new Account());
        assertThat(success).isFalse();
    }

    @Test
    void givenSameAccount_ShouldNotBeAllowed() {
        Account mockAccount = new Account();
        mockAccount.setId(1L);

        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));

        boolean success = accountService.deleteAccount(1L, mockAccount);

        assertThat(success).isFalse();
    }

    @Test
    void givenEmptyCustomerCode_NoNotChangeApproval() {
        Account mockAccount = new Account();
        mockAccount.setId(-1L);

        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));

        boolean success = accountService.changeApprovalStatus(1L, true, mockAccount);

        assertThat(success).isFalse();
    }

    @Test
    void givenWrongCustomerCode_DoNotChange() {
        Account mockAccount = new Account();
        mockAccount.setId(-1L);

        when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));

        List<String> codes = List.of("CORRECT_CODE");

        when(adminShipmentService.getAllCodes(Optional.of(mockAccount))).thenReturn(Optional.of(codes));

        boolean success = accountService.changeCode(1L, "WRONG_CODE", mockAccount);

        assertThat(success).isFalse();
    }
}
