package com.servermonitor.monitor.service;

import com.servermonitor.monitor.dto.OperatorDto;
import com.servermonitor.monitor.dto.ServerRequest;
import com.servermonitor.monitor.dto.ServerResponse;
import com.servermonitor.monitor.exception.ResourceNotFoundException;
import com.servermonitor.monitor.model.Server;
import com.servermonitor.monitor.repository.OperatorRepository;
import com.servermonitor.monitor.repository.ServerRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepository serverRepository;
    private final OperatorRepository operatorRepository;

    public List<ServerResponse> getAllServers() {
        return serverRepository.findAll().stream().map(this::toServerRespond).toList();
    }

    public ServerResponse getServerById(String id) {
        return toServerRespond(serverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Server ID: " + id)));
    }

    public ServerResponse addServer(ServerRequest request) {
        Server server = Server.builder()
                .name(request.getName())
                .endpoint(request.getEndpoint())
                .isMonitored(request.getIsMonitored() != null ? request.getIsMonitored() : true)
                .build();
        return toServerRespond(serverRepository.save(server));
    }

    public ServerResponse updateServer(String id, ServerRequest request) {
        Server existingServer = serverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("์Not Found Server ID: " + id));

        existingServer.setName(request.getName());
        existingServer.setEndpoint(request.getEndpoint());

        if (request.getIsMonitored() != null) {
            existingServer.setIsMonitored(request.getIsMonitored());
        }

        return toServerRespond(serverRepository.save(existingServer));
    }

    public String deleteServer(String id) {
        Server existingServer = serverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("์Not Found Server ID: " + id));

        serverRepository.delete(existingServer);

        return "Server ID " + id + " Deleted successfully";
    }

    private ServerResponse toServerRespond(Server server){
        List<OperatorDto> operators = server.getServerOperators()
                .stream()
                .map(so -> OperatorDto.builder()
                        .id(so.getOperator().getId())
                        .name(so.getOperator().getFirstName() + " " + so.getOperator().getLastName())
                        .build())
                .toList();

        return ServerResponse.builder()
                .id(server.getId())
                .name(server.getName())
                .endpoint(server.getEndpoint())
                .isMonitored(server.getIsMonitored())
                .operators(operators)
                .build();
    }
}
