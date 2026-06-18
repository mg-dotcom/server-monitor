package com.servermonitor.monitor.service;

import com.servermonitor.monitor.model.Log;
import com.servermonitor.monitor.model.Server;
import com.servermonitor.monitor.model.ServerStatus;
import com.servermonitor.monitor.repository.LogRepository;
import com.servermonitor.monitor.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitorService {
    private final ServerRepository serverRepository;
    private final RestClient restClient;
    private final LogRepository logRepository;
    private final LineNotifyService LineNotifyService;

    //    @Scheduled(fixedRate = 300000) // 5 นาที
    @Scheduled(fixedRate = 10000) // test - 10 วิ
    public void checkServers() {
        List<Server> servers = serverRepository.findAll();

        for (Server server : servers) {
            try {
                restClient.get().uri(server.getEndpoint()).retrieve().toBodilessEntity();
                saveLogIfChanged(server, ServerStatus.UP, "Health check passed");
            } catch (Exception e) {
                Boolean isLogChanged = saveLogIfChanged(server, ServerStatus.DOWN, e.getMessage());
                if(isLogChanged){
                    String alertMessage = """
                    🚨 [ALERT] Server DOWN!
                       - Server: %s
                       - URL: %s
                       - Error: %s
                    """.formatted(server.getName(), server.getEndpoint(), e.getMessage());
                    LineNotifyService.sendMessage(alertMessage);
                }
            }
        }
    }

    private Boolean saveLogIfChanged(Server server, ServerStatus status, String detail) {
        Log latestLog = logRepository.findByServerIdOrderByCreatedAtDesc(server.getId());

        if (latestLog == null || latestLog.getStatus() != status) {
            Log log = Log.builder()
                    .status(status)
                    .detail(detail)
                    .server(server)
                    .build();
            logRepository.save(log);
            return true;
        }
        return false;
    }
}
