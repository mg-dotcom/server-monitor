package com.servermonitor.monitor.mapper;

import com.servermonitor.monitor.dto.user.UserResponse;
import com.servermonitor.monitor.model.User;

public final class UserMapper {
    private UserMapper() {}

    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getFirstName() + " " + user.getLastName())
                .lineUserId(user.getLineUserId())
                .role(user.getRole())
                .build();
    }
}
