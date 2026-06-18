package com.servermonitor.monitor.repository;

import com.servermonitor.monitor.model.Log;
import com.servermonitor.monitor.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
    Log findByServerIdOrderByCreatedAtDesc(String serverId);
}
