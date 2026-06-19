package com.servermonitor.monitor.service;

import com.servermonitor.monitor.model.Log;
import com.servermonitor.monitor.model.Server;
import com.servermonitor.monitor.model.ServerStatus;
import com.servermonitor.monitor.repository.LogRepository;
import com.servermonitor.monitor.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
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
        List<Server> servers = serverRepository.findByIsMonitoredIsTrue();

        for (Server server : servers) {
            try {
                restClient.get().uri(server.getEndpoint()).retrieve().toBodilessEntity();
                boolean isRecovered = saveLogIfChanged(server, ServerStatus.UP, "Health check passed");
                if (isRecovered) {
                    String alertMessage = """
                ✅ [RECOVERY] Server is UP again!
                   - Server: %s
                   - URL: %s
                """.formatted(server.getName(), server.getEndpoint());
                    LineNotifyService.sendMessage(alertMessage);
                }
            } catch (Exception e) {
                boolean isLogChanged = saveLogIfChanged(server, ServerStatus.DOWN, e.getMessage());
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

        // Case 1: เพิ่งเคยเช็คครั้งแรกสุด
        if (latestLog == null) {
            logRepository.save(saveLogToDb(status,detail,server));

            return status == ServerStatus.DOWN;
        }

        // Case 2: มี Log เดิมอยู่แล้ว และสถานะ "เปลี่ยนไป" จากรอบที่แล้ว
        if (latestLog.getStatus() != status) {
            logRepository.save(saveLogToDb(status,detail,server));

            return true;
        }

        // Case 3: สถานะเหมือนเดิมตลอก
        return false;
    }

    private Log saveLogToDb(ServerStatus status, String detail, Server server){
        return  Log.builder()
                .status(status)
                .detail(detail)
                .server(server)
                .build();
    }
}
