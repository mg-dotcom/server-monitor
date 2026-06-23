package com.servermonitor.monitor.dto.server;

import com.servermonitor.monitor.dto.user.UserResponse;
import com.servermonitor.monitor.model.ServerStatus;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ServerResponse {
    private String id;
    private String name;
    private String endpoint;
    private Boolean isMonitored;
    private List<UserResponse> operators;
    private ServerStatus currentStatus;
}