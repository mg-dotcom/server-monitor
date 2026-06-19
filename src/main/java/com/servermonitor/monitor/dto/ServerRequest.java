package com.servermonitor.monitor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class ServerRequest {
    @NotBlank(message = "Please specify the Server name")
    private String name;
    @NotBlank(message = "Please specify the Endpoint")
    @URL(message = "Invalid URL format (must start with http:// or https://)")
    private String endpoint;
    private Boolean isMonitored;
}