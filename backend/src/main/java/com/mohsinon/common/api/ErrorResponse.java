package com.mohsinon.common.api;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private boolean success;

    private int status;

    private String error;

    private String message;

    private String path;

}