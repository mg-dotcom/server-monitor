package com.servermonitor.monitor.dto.line;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class ListLineMessageRequest {
    private String to;
    private List<LineMessageRequest> messages;
}
