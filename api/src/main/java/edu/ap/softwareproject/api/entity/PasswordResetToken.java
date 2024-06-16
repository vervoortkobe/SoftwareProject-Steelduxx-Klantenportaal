package edu.ap.softwareproject.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reset_tokens")
public class PasswordResetToken {

  private static final int EXPIRATION = 30;
  @Id
  @UuidGenerator
  private String token;

  @OneToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private Account user;

  private LocalDateTime expiryDate;

  public PasswordResetToken(Account user) {
    this.user = user;
    this.expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION);
  }
}