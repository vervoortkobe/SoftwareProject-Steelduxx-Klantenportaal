package edu.ap.softwareproject.api.service;

import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.entity.PasswordResetToken;
import edu.ap.softwareproject.api.repository.AccountRepository;
import edu.ap.softwareproject.api.repository.PasswordResetRepository;
import edu.ap.softwareproject.api.security.SecurityConfiguration;
import edu.ap.softwareproject.api.util.MailUtil;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * A service for resetting passwords and requesting a reset.
 */
@Service
public class PasswordResetService {
  private final PasswordResetRepository passwordResetRepository;
  private final AccountRepository accountRepository;

  public PasswordResetService(PasswordResetRepository passwordResetRepository, AccountRepository accountRepository) {
    this.passwordResetRepository = passwordResetRepository;
    this.accountRepository = accountRepository;
  }

  /**
   * Creates and sends a reset token to a given email.
   * @param email The email of the user.
   */
  public void requestTokenForEmail(String email) {
    // Find user by email.
    Optional<Account> account = accountRepository.findByEmail(email);
    if (account.isPresent()) {
      // Save new token
      Account presentAccount = account.get();
      PasswordResetToken token = new PasswordResetToken(presentAccount);

      Optional<PasswordResetToken> alreadyExisting = passwordResetRepository.findByUserId(presentAccount.getId());
        alreadyExisting.ifPresent(passwordResetRepository::delete);

      passwordResetRepository.save(token);

      // Send it to them via mail
      String domain = System.getenv("DOMAIN");
      if (domain == null)
        domain = "localhost:4200";

      MailUtil.sendMailPasswordReset(
          presentAccount.getEmail(),
          presentAccount.getAccount_information().getCompany_name(),
          "https://" + domain + "/reset/" + token.getToken());
    }
  }

  /**
   * Resets a password with a valid token.
   * @param uuid The token used for resetting.
   * @param newPassword The new password to be set for the user.
   * @return If the operation completed successfully.
   */
  public boolean resetPasswordWithToken(UUID uuid, String newPassword) {
    // Clear all other tokens

    // Get token from DB.
    Optional<PasswordResetToken> token = passwordResetRepository.findById(uuid.toString());
    // If exists, reset password with new one
    if (token.isPresent() && token.get().getExpiryDate().isAfter(LocalDateTime.now())) {
      Account account = token.get().getUser();
      account.setPassword(SecurityConfiguration.passwordEncoder().encode(newPassword));
      passwordResetRepository.deleteById(uuid.toString());

      accountRepository.save(account);
      return true;
    } else if (token.isPresent() && token.get().getExpiryDate().isBefore(LocalDateTime.now())) {
      passwordResetRepository.delete(token.get());
      return false;
    }
    return false;
  }
}
