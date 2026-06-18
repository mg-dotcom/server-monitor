package com.servermonitor.monitor.controller;

import com.servermonitor.monitor.dto.ApiResponse;
import com.servermonitor.monitor.dto.ServerRequest;
import com.servermonitor.monitor.model.Server;
import com.servermonitor.monitor.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {
    private final ServerService serverService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Server>>> getAllServer() {
        return ResponseEntity.ok(ApiResponse.ok(serverService.getAllServers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Server>> getServerById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(serverService.getServerById(id)));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Server>> addServer(@RequestBody ServerRequest request){
        return ResponseEntity.ok(ApiResponse.created(serverService.addServer(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Server>> updeteServer(@PathVariable String id, @RequestBody ServerRequest request) {
        return ResponseEntity.ok(ApiResponse.created(serverService.updateServer(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteServer(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(serverService.deleteServer(id)));
    }
}
