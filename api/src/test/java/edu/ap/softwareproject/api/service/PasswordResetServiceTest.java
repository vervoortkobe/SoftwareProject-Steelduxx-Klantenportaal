package edu.ap.softwareproject.api.service;

import edu.ap.softwareproject.api.entity.Account;
import edu.ap.softwareproject.api.entity.PasswordResetToken;
import edu.ap.softwareproject.api.repository.AccountRepository;
import edu.ap.softwareproject.api.repository.PasswordResetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PasswordResetServiceTest {
  @Mock
  private PasswordResetRepository passwordResetRepository;
  @Mock
  private AccountRepository accountRepository;
  private PasswordResetService passwordResetService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.passwordResetService = new PasswordResetService(passwordResetRepository, accountRepository);
  }

  @Test
  void givenExpiredToken_ShouldFail() {
    UUID random = UUID.randomUUID();

    when(passwordResetRepository.findById(any())).thenReturn(
        Optional.of(new PasswordResetToken(random.toString(), new Account(), LocalDateTime.now().minusHours(1))));

    assertThat(passwordResetService.resetPasswordWithToken(random, "test")).isFalse();
  }
}
