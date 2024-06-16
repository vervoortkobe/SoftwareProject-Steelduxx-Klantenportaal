package edu.ap.softwareproject.api.controller;

import edu.ap.softwareproject.api.dto.EmailDTO;
import edu.ap.softwareproject.api.dto.PasswordResetDTO;
import edu.ap.softwareproject.api.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password-reset")
@AllArgsConstructor
public class ResetPasswordController {

  private PasswordResetService passwordResetService;

  @Operation(summary = "Requests a token, and sends it to a valid user's email address.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "The token has been sent (if the account exists)."),
  })
  @PostMapping("request-token")
  public ResponseEntity<Void> requestTokenForEmail(
          @RequestBody EmailDTO email) {
    passwordResetService.requestTokenForEmail(email.getEmail());
    // We won't tell them if it failed or not, otherwise they know if the email
    // exists or not.
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Resets the password of an account.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "The account password has been reset."),
          @ApiResponse(responseCode = "401", description = "The reset token does not exist."),
  })
  @PostMapping("reset-password")
  public ResponseEntity<Void> resetPasswordWithToken(
          @RequestBody PasswordResetDTO reset) {
    boolean success = passwordResetService.resetPasswordWithToken(reset.getToken(), reset.getNewPassword());
    if (success)
      return ResponseEntity.ok().build();
    return ResponseEntity.status(401).build();
  }
}
