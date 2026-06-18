package com.servermonitor.monitor.service;

import com.servermonitor.monitor.dto.ServerRequest;
import com.servermonitor.monitor.exception.ResourceNotFoundException;
import com.servermonitor.monitor.model.Server;
import com.servermonitor.monitor.repository.ServerRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepository serverRepository;

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    public Server getServerById(String id) {
        return serverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Server ID: " + id));
    }

    public Server addServer(ServerRequest request){
        Server server = Server.builder()
                .name(request.getName())
                .endpoint(request.getEndpoint())
                .isActive(request.getIsActive())
                .build();
        return serverRepository.save(server);
    }

    public Server updateServer(String id, ServerRequest request) {
        Server existingServer = serverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("์Not Found Server ID: " + id));

        existingServer.setName(request.getName());
        existingServer.setEndpoint(request.getEndpoint());
        existingServer.setIsActive(request.getIsActive());

        return serverRepository.save(existingServer);
    }

    public String deleteServer(String id){
        Server existingServer = serverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("์Not Found Server ID: " + id));

        serverRepository.delete(existingServer);

        return "Server ID " + id + " Deleted successfully";
    }
}
