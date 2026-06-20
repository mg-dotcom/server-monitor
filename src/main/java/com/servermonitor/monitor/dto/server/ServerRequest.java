package com.servermonitor.monitor.dto.server;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import lombok.Data;

@Data
public class ServerRequest {
    @NotBlank(message = "Server name is required")
    private String name;
    @NotBlank(message = "Endpoint is required")
    @URL(message = "Endpoint must be a valid URL (e.g., http://example.com)")
    private String endpoint;
    private Boolean isMonitored;
}