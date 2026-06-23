package com.servermonitor.monitor.repository;

import com.servermonitor.monitor.model.User;
import com.servermonitor.monitor.model.Server;
import com.servermonitor.monitor.model.ServerOperator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // 👈 อย่าลืม import ตัวนี้ด้วยนะครับ

public interface ServerOperatorRepository extends JpaRepository<ServerOperator, Long> {
    boolean existsByServerAndOperator(Server server, User operator);

    Optional<ServerOperator> findByServerAndOperator(Server server, User operator);
}