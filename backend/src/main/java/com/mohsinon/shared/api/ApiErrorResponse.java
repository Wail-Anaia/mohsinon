package com.mohsinon.shared.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "الاستجابة الموحدة للأخطاء")
public class ApiErrorResponse {

    @Schema(description = "وقت حدوث الخطأ", example = "2026-07-17T10:45:21Z")
    private Instant timestamp;

    @Schema(description = "رمز HTTP", example = "400")
    private int status;

    @Schema(description = "نوع الخطأ", example = "Validation Error")
    private String error;

    @Schema(description = "رسالة الخطأ", example = "Validation failed.")
    private String message;

    @Schema(description = "مسار الطلب", example = "/api/v1/mosques")
    private String path;

    @Schema(description = "تفاصيل أخطاء التحقق")
    private List<ApiValidationError> errors;
}