package com.servermonitor.monitor.service;

import com.servermonitor.monitor.model.Server;
import com.servermonitor.monitor.repository.ServerRepository;
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

    public Server addServer(Server server){
        serverRepository.save(server);
        return server;
    }

    public Server updateServer(String id, Server updatedServer) {
        Server existingServer = serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("์Not Found Server ID: " + id));

        existingServer.setName(updatedServer.getName());
        existingServer.setEndpoint(updatedServer.getEndpoint());
        existingServer.setIsActive(updatedServer.getIsActive());

        return serverRepository.save(existingServer);
    }

    public void deleteServer(String id){
        Server existingServer = serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("์Not Found Server ID: " + id));

        serverRepository.deleteById(id);
    }
}
