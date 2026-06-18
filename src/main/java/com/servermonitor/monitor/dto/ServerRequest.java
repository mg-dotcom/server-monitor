package com.servermonitor.monitor.dto;

import lombok.Data;

@Data
public class ServerRequest {
    private String name;
    private String endpoint;
    private Boolean isActive;
}
