package com.servermonitor.monitor.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeResponse {
    private String username;
    private String role;
}