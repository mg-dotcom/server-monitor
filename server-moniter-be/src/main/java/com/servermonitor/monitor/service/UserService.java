package com.servermonitor.monitor.service;

import com.servermonitor.monitor.dto.user.UserRequest;
import com.servermonitor.monitor.dto.user.UserResponse;
import com.servermonitor.monitor.dto.user.UserUpdateRequest;
import com.servermonitor.monitor.exception.ConflictException;
import com.servermonitor.monitor.exception.ResourceNotFoundException;
import com.servermonitor.monitor.mapper.UserMapper;
import com.servermonitor.monitor.model.*;
import com.servermonitor.monitor.repository.*;
import com.servermonitor.monitor.utils.LinePushMessageToOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ServerOperatorRepository serverOperatorRepository;
    private final ServerRepository serverRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogRepository logRepository;
    private final LinePushMessageToOperator linePushMessageToOperator;

    public List<UserResponse> getAllOperators() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public UserResponse getOperatorById(String id) {
        return userRepository.findById(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found User ID: " + id));
    }

    public UserResponse createOperator(UserRequest request) {
        User operator = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .lineUserId(request.getLineUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        return UserMapper.toResponse(userRepository.save(operator));
    }

    public UserResponse updateOperator(String id, UserUpdateRequest request) {
        User existingOperator = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found User ID: " + id));

        existingOperator.setFirstName(request.getFirstName());
        existingOperator.setLastName(request.getLastName());
        existingOperator.setLineUserId(request.getLineUserId());

        return UserMapper.toResponse(userRepository.save(existingOperator));
    }

    public void deleteOperator(String id) {
        User existingOperator = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found User ID: " + id));

        userRepository.delete(existingOperator);
    }

    public void assignToServer(String serverId, String operatorId) {
        Server existingServer = serverRepository.findById(serverId)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Server ID: " + serverId));

        User existingOperator = userRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found User ID: " + operatorId));

        if (serverOperatorRepository.existsByServerAndOperator(existingServer, existingOperator)) {
            throw new ConflictException("Already assigned");
        }

        ServerOperator serverOperator = ServerOperator.builder()
                .server(existingServer)
                .operator(existingOperator)
                .build();

        serverOperatorRepository.save(serverOperator);

        Log latestLog = logRepository
                .findFirstByServerIdOrderByCreatedAtDesc(serverId)
                .orElse(null);

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

        User existingOperator = userRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found User ID: " + operatorId));

        ServerOperator serverOperator = serverOperatorRepository.findByServerAndOperator(existingServer, existingOperator)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found between Server ID: " + serverId + " and User ID: " + operatorId));

        serverOperatorRepository.delete(serverOperator);
    }
}
