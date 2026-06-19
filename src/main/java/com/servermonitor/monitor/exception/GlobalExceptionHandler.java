package com.servermonitor.monitor.exception;

import com.servermonitor.monitor.dto.ApiResponse;
import com.servermonitor.monitor.dto.ValidationErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(401)
                .body(ApiResponse.error(401, "Invalid username or password"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(400)
                .body(ApiResponse.error(400, e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(404)
            .body(ApiResponse.error(404, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ValidationErrorResponse.FieldErrorDetail> errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    ValidationErrorResponse.FieldErrorDetail detail = new ValidationErrorResponse.FieldErrorDetail();
                    detail.setField(error.getField());
                    detail.setErrorMessage(error.getDefaultMessage());
                    return detail;
                })
                .collect(Collectors.toList());

        ValidationErrorResponse response = new ValidationErrorResponse();
        response.setStatus(400);
        response.setMessage("Validation Failed");
        response.setErrors(errorDetails);

        return ResponseEntity.status(400).body(response);
    }
}
