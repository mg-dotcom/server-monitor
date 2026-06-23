package com.servermonitor.monitor.dto.user;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String lineUserId;
}
