package com.servermonitor.monitor.dto.operator;

import lombok.Data;

@Data
public class OperatorUpdateRequest {
    private String firstName;
    private String lastName;
    private String lineUserId;
}
