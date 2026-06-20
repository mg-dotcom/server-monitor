package com.servermonitor.monitor.repository;

import com.servermonitor.monitor.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server, String> {
    List<Server> findByIsMonitoredIsTrue();

    boolean existsByEndpoint(String endpoint);
}
