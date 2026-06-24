package com.servermonitor.monitor.repository;

import com.servermonitor.monitor.model.Operator;
import com.servermonitor.monitor.model.Server;
import com.servermonitor.monitor.model.ServerOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface ServerOperatorRepository extends JpaRepository<ServerOperator, Long> {
    boolean existsByServerAndOperator(Server server, Operator operator);

    Optional<ServerOperator> findByServerAndOperator(Server server, Operator operator);
}