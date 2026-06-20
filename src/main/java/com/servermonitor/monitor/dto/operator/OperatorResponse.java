package com.servermonitor.monitor.dto.operator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperatorResponse {
    private String id;
    private String name;
    private String lineUserId;
}

