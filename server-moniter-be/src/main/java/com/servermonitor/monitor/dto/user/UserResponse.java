package com.servermonitor.monitor.dto.user;

import com.servermonitor.monitor.model.Role; // 👈 อย่าลืม Import Role
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String lineUserId;
    private Role role;
}