package edu.ap.softwareproject.api.service;

import edu.ap.softwareproject.api.dto.*;
import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.entity.AccountInformation;
import edu.ap.softwareproject.api.enums.RegisterExceptionReason;
import edu.ap.softwareproject.api.enums.Role;
import edu.ap.softwareproject.api.exceptions.RegisterException;
import edu.ap.softwareproject.api.repository.AccountRepository;
import edu.ap.softwareproject.api.repository.AccountInformationRepository;
import edu.ap.softwareproject.api.security.JWTGenerator;
import edu.ap.softwareproject.api.util.MailUtil;
import edu.ap.softwareproject.api.util.UserValidationUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.ap.softwareproject.api.util.AuthUtil.getJWTToken;

/**
 * The service that handles account actions, this includes all user data as well
 * as utility functions.
 */
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountInformationRepository informationRepository;
    private final CountryService countryService;
    private final JWTGenerator jwtGenerator;
    private final AdminShipmentService adminShipmentService;
    private final AuthenticationManager authenticationManager;

    public AccountService(AccountRepository accountRepository,
            AccountInformationRepository informationRepository,
            CountryService countryService,
            JWTGenerator jwtGenerator,
            AdminShipmentService adminShipmentService,
            AuthenticationManager authenticationManager) {
        this.accountRepository = accountRepository;
        this.informationRepository = informationRepository;
        this.countryService = countryService;
        this.jwtGenerator = jwtGenerator;
        this.adminShipmentService = adminShipmentService;
        this.authenticationManager = authenticationManager;
    }

    @Value("${expiredays}")
    private int expiredays;

    /**
     * Find an account by Id.
     * 
     * @param id The Id of the account.
     * @return The account.
     */
    public Optional<AccountApprovalDTO> findAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(value -> new AccountApprovalDTOMapper().apply(value));
    }

    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    /**
     * Allows the user to log in.
     * 
     * @param user A LoginDTO containing the username and password of the user.
     * @return A JWT token for authentication.
     */
    public Optional<ResponseCookie> login(LoginDTO user) {
        Authentication authentication;

        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        Optional<Account> account = this.findAccountByEmail(user.getEmail());
        if (account.isPresent() && account.get().getApproved()) {

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            ResponseCookie responseCookie = ResponseCookie.from("JWT", token)
                    .httpOnly(true)
                    .maxAge((long) expiredays * 24 * 60 * 60)
                    .path("/")
                    .sameSite("strict")
                    .build();

            return Optional.of(responseCookie);
        }
        return Optional.empty();
    }

    /**
     * Registers a user with the default role of "KLANT", non-approved.
     * 
     * @param user The user information.
     * @throws RegisterException
     */
    public void registerUser(AccountCreationDTO user) throws RegisterException {
        // Register a user, not approved, with role "KLANT".

        /*
         * APPLY VALIDATION HERE.
         * Strong password + other fields that haven't been implemented here
         * Check if email is valid
         * Check if user doesn't already exist.
         * Validate any extra fields
         * - Telephone (if supplied)
         * - Valid country
         * - VAT number validation (if supplied)
         * - City validate + uppercase first letter
         */

        AccountInformationDTO informationDTO = user.getAccountInformation();

        if (this.findAccountByEmail(user.getEmail()).isPresent())
            throw new RegisterException(RegisterExceptionReason.ALREADY_EXISTS);

        if (UserValidationUtil.validateEmail(user.getEmail())
                && (informationDTO.getContact_telephone().isBlank()
                        || UserValidationUtil.validatePhoneNumber(informationDTO.getContact_telephone())
                                && countryService.countryExistsById(informationDTO.getCountryId())
                                && (informationDTO.getBtw_number().isBlank()
                                        || UserValidationUtil.validateVAT(informationDTO.getBtw_number())))) {

            AccountCreationMapper mapper = new AccountCreationMapper();
            AccountInformationMapper infoMapper = new AccountInformationMapper();

            Account account = mapper.apply(user);

            account = accountRepository.save(account);

            AccountInformation information = infoMapper.apply(user.getAccountInformation());
            information.setAccounts_id(account.getId());

            informationRepository.save(information);

        } else {
            throw new RegisterException(RegisterExceptionReason.VALIDATION_FAILED);
        }
    }

    /**
     * Gets an account from a JWT token.
     * 
     * @param jwt The JWT token used for authentication.
     * @return The account linked to the JWT token.
     */
    private Optional<Account> getAccountFromJWT(Optional<Cookie> jwt) {
        if (jwt.isPresent() && jwtGenerator.validateToken(jwt.get().getValue())) {

            String email = jwtGenerator.getEmailFromJWT(jwt.get().getValue());

            Optional<Account> user = findAccountByEmail(email);

            if (user.isPresent())
                return user;
        }
        return Optional.empty();
    }

    /**
     * Extracts an account from a request.
     * 
     * @param request The request containing the JWT token.
     * @return The account linked to the JWT token from the request.
     */
    public Optional<Account> getAccountFromRequest(HttpServletRequest request) {
        return getAccountFromJWT(
                getJWTToken(request.getCookies()));
    }

    public List<AccountApprovalDTO> getApprovalForAccounts() {
        AccountApprovalDTOMapper accountApprovalDTOMapper = new AccountApprovalDTOMapper();

        List<AccountApprovalDTO> approvalDTO = new ArrayList<>();
        accountRepository.findAll().forEach(
                item -> approvalDTO.add(accountApprovalDTOMapper.apply(item)));

        return approvalDTO;
    }

    /**
     * Changes the approval status of an account by Id.
     * 
     * @param id           The id of the account.
     * @param approval     The approval status of the account to set it to.
     * @param ownerAccount The account making the request.
     * @return If the operation succeeded successfully.
     */
    public boolean changeApprovalStatus(Long id, Boolean approval, Account ownerAccount) {
        Optional<Account> account = accountRepository.findById(id);

        if (account.isPresent() && !account.get().getId().equals(ownerAccount.getId()) && account.get().getId() != -1
                && account.get().getCustomerCode() != null && !account.get().getCustomerCode().isEmpty()) {
            Account accountChange = account.get();
            accountChange.setApproved(approval);

            // Depending on the status send an email.
            if (Boolean.TRUE.equals(approval)) {
                String accountName = "ADMINISTRATOR.";

                AccountInformation accountInformation = accountChange.getAccount_information();

                if (accountInformation != null) {
                    accountName = accountInformation.getCompany_name();
                }

                String domain = System.getenv("DOMAIN");
                if (domain == null)
                    domain = "localhost:4200";

                MailUtil.sendMailRegisteredAccount(account.get().getEmail(), accountName,
                        "https://" + domain + "/login");
            }

            accountRepository.save(accountChange);
            return true;
        }
        return false;
    }

    /**
     * Changes the role of a user by account Id.
     * 
     * @param id           The Id of the account.
     * @param role         The role to change the account to.
     * @param ownerAccount The account making the request.
     * @return If the operation succeeded successfully.
     */
    public boolean changeRole(Long id, Role role, Account ownerAccount) {
        Optional<Account> account = accountRepository.findById(id);

        if (account.isPresent() && !account.get().getId().equals(ownerAccount.getId()) && account.get().getId() != -1) {
            Account accountChange = account.get();
            accountChange.setRole(role);

            accountRepository.save(accountChange);
            return true;
        }
        return false;
    }

    /**
     * Changes the customer code by account Id.
     * 
     * @param id           The account Id.
     * @param code         A valid customer code.
     * @param ownerAccount The account making the request.
     * @return If the operation succeeded successfully.
     */
    public boolean changeCode(Long id, String code, Account ownerAccount) {
        Optional<Account> account = accountRepository.findById(id);

        Optional<List<String>> validCodesOptional = adminShipmentService.getAllCodes(Optional.of(ownerAccount));

        if (validCodesOptional.isPresent()) {
            List<String> validCodes = validCodesOptional.get();

            if (account.isPresent()
                    && !account.get().getId().equals(ownerAccount.getId())
                    && account.get().getId() != -1
                    && validCodes.contains(code)) {
                Account accountChange = account.get();
                accountChange.setCustomerCode(code);

                accountRepository.save(accountChange);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes an account by Id.
     * 
     * @param id           The Id of the account.
     * @param ownerAccount The account making the request.
     * @return If the operation succeeded successfully.
     */
    public boolean deleteAccount(Long id, Account ownerAccount) {
        Optional<Account> account = accountRepository.findById(id);

        if (account.isPresent() && !account.get().getId().equals(ownerAccount.getId()) && account.get().getId() != -1) {
            accountRepository.delete(account.get());
            return true;
        }
        return false;
    }
}
