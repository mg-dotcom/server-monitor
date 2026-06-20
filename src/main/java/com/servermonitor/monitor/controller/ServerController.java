package com.servermonitor.monitor.controller;

import com.servermonitor.monitor.ApiResponse.ApiResponse;
import com.servermonitor.monitor.dto.server.ServerRequest;
import com.servermonitor.monitor.dto.server.ServerResponse;
import com.servermonitor.monitor.service.OperatorService;
import com.servermonitor.monitor.service.ServerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {
    private final ServerService serverService;
    private final OperatorService operatorService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ServerResponse>>> getAllServer() {
        return ResponseEntity.ok(ApiResponse.ok(serverService.getAllServers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServerResponse>> getServerById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(serverService.getServerById(id)));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<ServerResponse>> addServer(@Valid @RequestBody ServerRequest request){
        return ResponseEntity.ok(ApiResponse.created(serverService.addServer(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ServerResponse>> updeteServer(@PathVariable String id,@Valid @RequestBody ServerRequest request) {
        return ResponseEntity.ok(ApiResponse.created(serverService.updateServer(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteServer(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(serverService.deleteServer(id)));
    }

    @PostMapping("/{serverId}/assign-operator/{operatorId}")
    public ResponseEntity<ApiResponse<ServerResponse>> assignOperator(
            @PathVariable String serverId,
            @PathVariable String operatorId) {
        operatorService.assignToServer(serverId, operatorId);
        return ResponseEntity.ok(ApiResponse.ok(serverService.getServerById(serverId)));
    }

    @DeleteMapping("/{serverId}/remove-operator/{operatorId}")
    public ResponseEntity<ApiResponse<ServerResponse>> removeOperator(
            @PathVariable String serverId,
            @PathVariable String operatorId) {
        operatorService.removeFromServer(serverId, operatorId);
        return ResponseEntity.ok(ApiResponse.ok(serverService.getServerById(serverId)));
    }
}
