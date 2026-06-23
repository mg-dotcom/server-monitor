package com.servermonitor.monitor.mapper;

import com.servermonitor.monitor.dto.operator.OperatorResponse;
import com.servermonitor.monitor.model.Operator;

public final class OperatorMapper {
    private OperatorMapper() {}

    public static OperatorResponse toResponse(Operator operator) {
        if (operator == null) {
            return null;
        }
        return OperatorResponse.builder()
                .id(operator.getId())
                .name(operator.getFirstName() + " " + operator.getLastName())
                .lineUserId(operator.getLineUserId())
                .role(operator.getRole())
                .build();
    }
}
