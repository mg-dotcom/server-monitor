package com.servermonitor.monitor.controller;

import com.servermonitor.monitor.ApiResponse.ApiResponse;
import com.servermonitor.monitor.dto.operator.OperatorRequest;
import com.servermonitor.monitor.dto.operator.OperatorResponse;
import com.servermonitor.monitor.dto.operator.OperatorUpdateRequest;
import com.servermonitor.monitor.service.OperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operators")
@RequiredArgsConstructor
public class OperatorController {

    private final OperatorService operatorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OperatorResponse>>> getAllOperators() {
        return ResponseEntity.ok(ApiResponse.ok(operatorService.getAllOperators()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OperatorResponse>> getOperatorById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(operatorService.getOperatorById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OperatorResponse>> createOperator(@RequestBody OperatorRequest request) {
        OperatorResponse created = operatorService.createOperator(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OperatorResponse>> updateOperator(@PathVariable String id, @RequestBody OperatorUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(operatorService.updateOperator(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOperator(@PathVariable String id) {
        operatorService.deleteOperator(id);
        return ResponseEntity.ok(ApiResponse.ok("Operator ID " + id + " Deleted successfully"));
    }
}