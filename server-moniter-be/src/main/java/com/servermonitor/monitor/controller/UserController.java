package com.servermonitor.monitor.controller;

import com.servermonitor.monitor.ApiResponse.ApiResponse;
import com.servermonitor.monitor.dto.user.UserRequest;
import com.servermonitor.monitor.dto.user.UserResponse;
import com.servermonitor.monitor.dto.user.UserUpdateRequest;
import com.servermonitor.monitor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operators")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllOperators() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getAllOperators()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getOperatorById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getOperatorById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createOperator(@RequestBody UserRequest request) {
        UserResponse created = userService.createOperator(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateOperator(@PathVariable String id, @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(userService.updateOperator(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteOperator(@PathVariable String id) {
        userService.deleteOperator(id);
        return ResponseEntity.ok(ApiResponse.ok("User ID " + id + " Deleted successfully"));
    }
}