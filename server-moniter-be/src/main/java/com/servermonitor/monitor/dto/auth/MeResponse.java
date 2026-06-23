package com.servermonitor.monitor.dto.auth;

import com.servermonitor.monitor.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeResponse {
    private String username;
    private Role role;
}