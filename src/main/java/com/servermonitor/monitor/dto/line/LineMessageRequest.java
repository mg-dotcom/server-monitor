package com.servermonitor.monitor.dto.line;

import lombok.*;

@Getter
@Builder
public class LineMessageRequest {
    private String type;
    private String text;
}
