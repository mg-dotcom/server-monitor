package com.servermonitor.monitor.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class ListLineMessageRequest {
    private List<LineMessageRequest> messages;
}
