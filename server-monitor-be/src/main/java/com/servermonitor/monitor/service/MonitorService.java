package com.servermonitor.monitor.service;

import com.servermonitor.monitor.model.*;
import com.servermonitor.monitor.repository.LogRepository;
import com.servermonitor.monitor.repository.ServerRepository;
import com.servermonitor.monitor.utils.LinePushMessageToOperator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonitorService {
    private final ServerRepository serverRepository;
    private final RestClient restClient;
    private final LogRepository logRepository;
    private final LinePushMessageToOperator linePushMessageToOperator;

    
    @Transactional
    @Scheduled(fixedRate = 30000)
    public void checkServers() {
        List<Server> servers = serverRepository.findByIsMonitoredIsTrue();
        for (Server server : servers) {
            try {
                restClient.get().uri(server.getEndpoint()).retrieve().toBodilessEntity();
                boolean statusChanged = saveLogIfChanged(server, ServerStatus.UP, "Health check passed");
                if (statusChanged) {
                    String recoveryMessage = """
                            ✅ [RECOVERY] Server is UP again!
                               - Server: %s
                               - URL: %s
                            """.formatted(server.getName(), server.getEndpoint());
                    linePushMessageToOperator.pushMessageToOperators(server, recoveryMessage);
                }
            } catch (Exception e) {
                boolean statusChanged = saveLogIfChanged(server, ServerStatus.DOWN, e.getMessage());
                if (statusChanged) {
                    String alertMessage = """
                            🚨 [ALERT] Server DOWN!
                               - Server: %s
                               - URL: %s
                               - Error: %s
                            """.formatted(server.getName(), server.getEndpoint(), e.getMessage());
                    linePushMessageToOperator.pushMessageToOperators(server, alertMessage);
                }
            }
        }
    }

    private Boolean saveLogIfChanged(Server server, ServerStatus status, String detail) {
        Optional<Log> latestLog = logRepository.findFirstByServerIdOrderByCreatedAtDesc(server.getId());

        if (latestLog.isEmpty()) {
            logRepository.save(saveLogToDb(status, detail, server));
            return status == ServerStatus.DOWN;
        }

        if (latestLog.get().getStatus() != status) {
            logRepository.save(saveLogToDb(status, detail, server));
            return true;
        }

        return false;
    }

    private Log saveLogToDb(ServerStatus status, String detail, Server server) {
        return Log.builder()
                .status(status)
                .detail(detail)
                .server(server)
                .build();
    }
}
