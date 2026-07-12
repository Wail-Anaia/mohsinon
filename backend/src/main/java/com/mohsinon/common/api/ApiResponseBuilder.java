package com.mohsinon.common.api;

public final class ApiResponseBuilder {

    private ApiResponseBuilder() {
    }

    public static <T> ApiResponse<T> success(
            String message,
            T data) {

        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> success(
            String message) {

        return ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .build();
    }
    
    public static ApiResponse<Void> deleted() {

        return ApiResponse.<Void>builder()
                .success(true)
                .message(ApiMessage.DELETED)
                .build();

    }

}