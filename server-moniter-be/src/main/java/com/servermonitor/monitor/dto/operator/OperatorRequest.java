package com.servermonitor.monitor.dto.operator;

import com.servermonitor.monitor.model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OperatorRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Role is required")
    private Role role;
    private String lineUserId;
}