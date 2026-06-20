package com.servermonitor.monitor.utils;

import com.servermonitor.monitor.model.Server;
import com.servermonitor.monitor.service.LineMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinePushMessageToOperator {
    private final LineMessagingService lineNotifyService;

    public void pushMessageToOperators(Server server, String alertMessage) {
        server.getServerOperators().stream()
                .filter(s -> s.getOperator().getLineUserId() != null)
                .forEach(
                        s -> lineNotifyService.sendMessage(s.getOperator().getLineUserId(), alertMessage)
                );
    }
}
