package com.servermonitor.monitor.dto.operator;

import com.servermonitor.monitor.model.Role; // 👈 อย่าลืม Import Role
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperatorResponse {
    private String id;
    private String name;
    private String lineUserId;
    private Role role;
}