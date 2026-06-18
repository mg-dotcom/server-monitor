package com.servermonitor.monitor.dto;

import lombok.*;

@Getter
@Builder
public class LineMessageRequest {
    private String type;
    private String text;
}
