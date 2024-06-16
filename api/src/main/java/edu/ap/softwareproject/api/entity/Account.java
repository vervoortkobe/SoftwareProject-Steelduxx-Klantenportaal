package edu.ap.softwareproject.api.entity;

import edu.ap.softwareproject.api.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A user account.
 */
@Entity
@Table(name = "accounts")
@EqualsAndHashCode
@Data
@AllArgsConstructor
public class Account {

    // ---- ATTRIBUTES ----
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;

    private Role role;
    @JoinColumn(name = "id")
    @OneToOne(fetch = FetchType.EAGER)
    private AccountInformation account_information;
    private Boolean approved;
    private String customerCode;

  public Account(String email, String password, Boolean approved, Role role) {
    this.email = email;
    this.password = password;
    this.approved = approved;
    this.role = role;
  }

    public Account() {
    }
    public boolean isAdmin() {
        return this.role.equals(Role.BEHEERDER);
    }
}
