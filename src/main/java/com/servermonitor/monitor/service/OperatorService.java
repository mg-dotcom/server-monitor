package com.servermonitor.monitor.service;

import com.servermonitor.monitor.dto.operator.OperatorRequest;
import com.servermonitor.monitor.dto.operator.OperatorResponse;
import com.servermonitor.monitor.dto.operator.OperatorUpdateRequest;
import com.servermonitor.monitor.exception.ConflictException;
import com.servermonitor.monitor.exception.ResourceNotFoundException;
import com.servermonitor.monitor.mapper.OperatorMapper;
import com.servermonitor.monitor.model.*;
import com.servermonitor.monitor.repository.LogRepository;
import com.servermonitor.monitor.repository.OperatorRepository;
import com.servermonitor.monitor.repository.ServerOperatorRepository;
import com.servermonitor.monitor.repository.ServerRepository;
import com.servermonitor.monitor.utils.LinePushMessageToOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperatorService {
    private final OperatorRepository operatorRepository;
    private final ServerOperatorRepository serverOperatorRepository;
    private final ServerRepository serverRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogRepository logRepository;
    private final LinePushMessageToOperator linePushMessageToOperator;

    public List<OperatorResponse> getAllOperators() {
        return operatorRepository.findAll()
                .stream()
                .map(OperatorMapper::toResponse)
                .toList();
    }

    public OperatorResponse getOperatorById(String id) {
        return operatorRepository.findById(id)
                .map(OperatorMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Operator ID: " + id));
    }

    public OperatorResponse createOperator(OperatorRequest request) {
        Operator operator = Operator.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .lineUserId(request.getLineUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        return OperatorMapper.toResponse(operatorRepository.save(operator));
    }

    public OperatorResponse updateOperator(String id, OperatorUpdateRequest request) {
        Operator existingOperator = operatorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Operator ID: " + id));

        existingOperator.setFirstName(request.getFirstName());
        existingOperator.setLastName(request.getLastName());
        existingOperator.setLineUserId(request.getLineUserId());

        return OperatorMapper.toResponse(operatorRepository.save(existingOperator));
    }

    public void deleteOperator(String id) {
        Operator existingOperator = operatorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Operator ID: " + id));

        operatorRepository.delete(existingOperator);
    }

    public void assignToServer(String serverId, String operatorId) {
        Server existingServer = serverRepository.findById(serverId)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Server ID: " + serverId));

        Operator existingOperator = operatorRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Operator ID: " + operatorId));

        if (serverOperatorRepository.existsByServerAndOperator(existingServer, existingOperator)) {
            throw new ConflictException("Already assigned");
        }

        ServerOperator serverOperator = ServerOperator.builder()
                .server(existingServer)
                .operator(existingOperator)
                .build();

        serverOperatorRepository.save(serverOperator);

        Log latestLog = logRepository
                .findByServerIdOrderByCreatedAtDesc(serverId)
                .stream().findFirst().orElse(null);

        if (latestLog != null && latestLog.getStatus() == ServerStatus.DOWN) {
            String alertMessage = """
            🚨 [ALERT] Server DOWN!
               - Server: %s
               - URL: %s
               - Error: %s
            """.formatted(existingServer.getName(), existingServer.getEndpoint(), latestLog.getDetail());
            linePushMessageToOperator.pushMessageToOperators(existingServer, alertMessage);
        }

    }

    public void removeFromServer(String serverId, String operatorId) {
        Server existingServer = serverRepository.findById(serverId)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Server ID: " + serverId));

        Operator existingOperator = operatorRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Operator ID: " + operatorId));

        ServerOperator serverOperator = serverOperatorRepository.findByServerAndOperator(existingServer, existingOperator)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found between Server ID: " + serverId + " and Operator ID: " + operatorId));

        serverOperatorRepository.delete(serverOperator);
    }
}
