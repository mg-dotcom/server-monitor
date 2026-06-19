package com.servermonitor.monitor.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ValidationErrorResponse {
    private int status;
    private String message;
    private List<FieldErrorDetail> errors;

    @Data
    @NoArgsConstructor
    public static class FieldErrorDetail {
        private String field;
        private String errorMessage;
    }
}