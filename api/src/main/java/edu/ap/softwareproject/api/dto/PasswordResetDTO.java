package edu.ap.softwareproject.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDTO {
    private UUID token;
    private String newPassword;
}
