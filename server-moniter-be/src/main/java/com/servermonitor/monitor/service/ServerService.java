package com.servermonitor.monitor.service;

import com.servermonitor.monitor.dto.user.UserResponse;
import com.servermonitor.monitor.dto.server.ServerRequest;
import com.servermonitor.monitor.dto.server.ServerResponse;
import com.servermonitor.monitor.exception.ConflictException;
import com.servermonitor.monitor.exception.ResourceNotFoundException;
import com.servermonitor.monitor.mapper.UserMapper;
import com.servermonitor.monitor.model.Log;
import com.servermonitor.monitor.model.Server;
import com.servermonitor.monitor.model.ServerStatus;
import com.servermonitor.monitor.repository.LogRepository;
import com.servermonitor.monitor.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepository serverRepository;
    private final LogRepository logRepository;

    public List<ServerResponse> getAllServers() {
        return serverRepository.findAll().stream().map(this::toServerRespond).toList();
    }

    public ServerResponse getServerById(String id) {
        return serverRepository.findById(id)
                .map(this::toServerRespond)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Server ID: " + id));
    }

    public ServerResponse addServer(ServerRequest request) {
        if (serverRepository.existsByEndpoint(request.getEndpoint())) {
            throw new ConflictException("Server endpoint already exists: " + request.getEndpoint());
        }

        Server server = Server.builder()
                .name(request.getName())
                .endpoint(request.getEndpoint())
                .isMonitored(request.getIsMonitored() != null ? request.getIsMonitored() : true)
                .build();
        return toServerRespond(serverRepository.save(server));
    }

    public ServerResponse updateServer(String id, ServerRequest request) {
        Server existingServer = serverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Server ID: " + id));

        existingServer.setName(request.getName());
        existingServer.setEndpoint(request.getEndpoint());

        if (request.getIsMonitored() != null) {
            existingServer.setIsMonitored(request.getIsMonitored());
        }

        return toServerRespond(serverRepository.save(existingServer));
    }

    public String deleteServer(String id) {
        Server existingServer = serverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Server ID: " + id));

        serverRepository.delete(existingServer);

        return "Server ID " + id + " Deleted successfully";
    }

    private ServerResponse toServerRespond(Server server) {

        List<UserResponse> operators = server.getServerOperators()
                .stream()
                .map(so -> UserMapper.toResponse(so.getOperator()))
                .toList();

        ServerStatus currentStatus = logRepository
                .findFirstByServerIdOrderByCreatedAtDesc(server.getId())
                .map(Log::getStatus)
                .orElse(ServerStatus.UNKNOWN);

        return ServerResponse.builder()
                .id(server.getId())
                .name(server.getName())
                .endpoint(server.getEndpoint())
                .isMonitored(server.getIsMonitored())
                .operators(operators)
                .currentStatus(currentStatus)
                .build();
    }
}
