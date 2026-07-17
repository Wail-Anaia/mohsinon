package com.mohsinon.shared.Annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@io.swagger.v3.oas.annotations.parameters.RequestBody(
    description = "بيانات الطلب",
    required = true
)
public @interface ApiRequestBody {
}