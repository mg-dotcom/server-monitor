package com.servermonitor.monitor.dto;

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
    private List<OperatorDto> operators;
}