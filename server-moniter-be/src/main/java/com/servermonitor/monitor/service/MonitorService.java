package com.servermonitor.monitor.service;

import com.servermonitor.monitor.dto.operator.OperatorResponse;
import com.servermonitor.monitor.model.*;
import com.servermonitor.monitor.repository.LogRepository;
import com.servermonitor.monitor.repository.ServerOperatorRepository;
import com.servermonitor.monitor.repository.ServerRepository;
import com.servermonitor.monitor.utils.LinePushMessageToOperator;
import jakarta.transaction.Transactional;
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
    private final LinePushMessageToOperator linePushMessageToOperator;

    //    @Scheduled(fixedRate = 300000) // 5 นาที
    @Transactional
    @Scheduled(fixedRate = 10000) // test - 10 วิ
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
                if (statusChanged) { // แปลว่าเพิ่งเปลี่ยนจาก UP → DOWN
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
        List<Log> logs = logRepository.findByServerIdOrderByCreatedAtDesc(server.getId());
        Log latestLog = logs.isEmpty() ? null : logs.getFirst();

        if (latestLog == null) {
            logRepository.save(saveLogToDb(status, detail, server));
            return status == ServerStatus.DOWN; // ครั้งแรก DOWN = alert
        }

        if (latestLog.getStatus() != status) {
            logRepository.save(saveLogToDb(status, detail, server));
            return true; // status เปลี่ยน = ต้อง alert
        }

        return false; // status เหมือนเดิม = ไม่ต้อง alert
    }

    private Log saveLogToDb(ServerStatus status, String detail, Server server) {
        return Log.builder()
                .status(status)
                .detail(detail)
                .server(server)
                .build();
    }
}
