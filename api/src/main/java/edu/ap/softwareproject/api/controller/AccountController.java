package edu.ap.softwareproject.api.controller;

import edu.ap.softwareproject.api.dto.*;
import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.enums.RegisterExceptionReason;
import edu.ap.softwareproject.api.exceptions.RegisterException;
import edu.ap.softwareproject.api.security.JWTGenerator;
import edu.ap.softwareproject.api.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static edu.ap.softwareproject.api.util.AuthUtil.getJWTToken;

@RestController
@RequestMapping("/users")
public class AccountController {
    private final AccountService accountService;
    private final JWTGenerator jwtGenerator;

    public AccountController(AccountService accountService,
            JWTGenerator jwtGenerator) {
        this.accountService = accountService;
        this.jwtGenerator = jwtGenerator;
    }

    @Operation(summary = "Gets a user by Id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valid request, account information is returned."),
            @ApiResponse(responseCode = "404", description = "The user does not exist.", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @GetMapping("")
    public ResponseEntity<AccountDTO> getUser(HttpServletRequest request) {
        Optional<Account> user = accountService.getAccountFromRequest(request);

        if (user.isPresent()) {
            AccountDTOMapper mapper = new AccountDTOMapper();

            return new ResponseEntity<>(mapper.apply(user.get()), HttpStatus.OK);
        }
        return ResponseEntity.status(404).build();
    }

    @Operation(summary = "Logs the user in and returns a JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The SET_COOKIE header will be set."),
            @ApiResponse(responseCode = "404", description = "The user does not exist."),
            @ApiResponse(responseCode = "401", description = "Incorrect login.")
    })
    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse response,
            @RequestBody LoginDTO user) {
        try {
            Optional<ResponseCookie> cookie = accountService.login(user);

            if (cookie.isPresent()) {
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.get().toString());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @Operation(summary = "Registers a new user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The user has been created successfully."),
            @ApiResponse(responseCode = "404", description = "The user already exists."),
            @ApiResponse(responseCode = "401", description = "The validation has failed.")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(HttpServletResponse response,
            @RequestBody AccountCreationDTO user) {
        try {
            accountService.registerUser(user);
        } catch (RegisterException e) {
            if (e.reason == RegisterExceptionReason.VALIDATION_FAILED)
                return ResponseEntity.badRequest().build();
            if (e.reason == RegisterExceptionReason.ALREADY_EXISTS)
                return ResponseEntity.status(409).build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Logs out the user by clearing the JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The cookie has been cleared successfully."),
    })
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Checks if the current user is authenticated by checking their JWT cookie.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The user is authenticated."),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated.")
    })
    @GetMapping("/authenticated")
    public ResponseEntity<Void> authenticated(HttpServletRequest request) {
        Optional<Cookie> jwt = getJWTToken(request.getCookies());
        if (jwt.isPresent() && jwtGenerator.validateToken(jwt.get().getValue())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(401).build();
    }

    @Operation(summary = "Lists all accounts in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A list of accounts."),
    })
    @GetMapping("/admin/approval")
    public ResponseEntity<List<AccountApprovalDTO>> getAllAccounts(HttpServletRequest request) {
        return new ResponseEntity<>(accountService.getApprovalForAccounts(), HttpStatus.OK);
    }

    @Operation(summary = "Changes the approval status for an account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The user's approval status has been changed."),
            @ApiResponse(responseCode = "400", description = "The request is not valid."),
            @ApiResponse(responseCode = "404", description = "The user does not exist.")
    })
    @PostMapping("/admin/approval")
    public ResponseEntity<Void> changeApprovalForAccount(HttpServletRequest request,
            @RequestBody AccountChangeApprovalDTO accountApprovalChange) {
        Optional<Account> user = accountService.getAccountFromRequest(request);
        if (user.isPresent()) {
            Boolean success = accountService.changeApprovalStatus(accountApprovalChange.getId(),
                    accountApprovalChange.getApproval(),
                    user.get());
            if (!success)
                return ResponseEntity.status(400).build();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).build();
    }

    @Operation(summary = "Changes the role for an account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The role has been changed."),
            @ApiResponse(responseCode = "404", description = "The user does not exist.")
    })
    @PostMapping("/admin/role")
    public ResponseEntity<Void> changeRoleForAccount(HttpServletRequest request,
            @RequestBody AccountChangeRoleDTO accountChangeRoleDTO) {
        Optional<Account> user = accountService.getAccountFromRequest(request);
        if (user.isPresent()) {
            accountService.changeRole(accountChangeRoleDTO.getId(), accountChangeRoleDTO.getRole(), user.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).build();
    }

    @Operation(summary = "Changes the customer code for an account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The user's customer code has been changed."),
            @ApiResponse(responseCode = "400", description = "The request is not valid."),
            @ApiResponse(responseCode = "404", description = "The user does not exist.")
    })
    @PostMapping("/admin/code")
    public ResponseEntity<Void> changeCodeForAccount(HttpServletRequest request,
            @RequestBody AccountChangeCodeDTO accountChangeCodeDTO) {
        Optional<Account> user = accountService.getAccountFromRequest(request);
        if (user.isPresent()) {
            boolean success = accountService.changeCode(accountChangeCodeDTO.getId(), accountChangeCodeDTO.getCode(),
                    user.get());
            if (!success)
                return ResponseEntity.status(400).build();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).build();
    }

    @Operation(summary = "Deletes an account by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The user has been deleted."),
            @ApiResponse(responseCode = "404", description = "The user does not exist.")
    })
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteAccount(HttpServletRequest request, @PathVariable("id") Long id) {
        Optional<Account> user = accountService.getAccountFromRequest(request);
        if (user.isPresent()) {
            accountService.deleteAccount(id, user.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).build();
    }

    @Operation(summary = "Requests the details of an account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The detailed user information."),
            @ApiResponse(responseCode = "404", description = "The user does not exist.", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @GetMapping("/admin/approval-detail/{id}")
    public ResponseEntity<AccountApprovalDTO> getAccount(HttpServletRequest request, @PathVariable("id") Long id) {
        Optional<AccountApprovalDTO> account = accountService.findAccountById(id);
        return account.map(accountApprovalDTO -> new ResponseEntity<>(accountApprovalDTO, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(404).build());
    }
}
