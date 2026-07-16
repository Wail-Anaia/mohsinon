package com.mohsinon.common.api;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private boolean success;

    private String message;

    /**
     * يستخدم فقط عند وجود أخطاء.
     */
    private String errorCode;

    /**
     * البيانات المرجعة.
     */
    private T data;

}