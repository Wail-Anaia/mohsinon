package com.mohsinon.shared.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "خطأ في أحد الحقول")
public class ApiValidationError {

    @Schema(description = "اسم الحقل", example = "name")
    private String field;

    @Schema(description = "رسالة الخطأ", example = "اسم المسجد مطلوب")
    private String message;
}