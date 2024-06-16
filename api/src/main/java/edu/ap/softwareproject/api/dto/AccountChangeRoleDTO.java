package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.enums.Role;
import lombok.Data;

@Data
public class AccountChangeRoleDTO {
    public Long id;
    public Role role;
}
