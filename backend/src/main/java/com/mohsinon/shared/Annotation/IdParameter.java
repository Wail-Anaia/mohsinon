package com.mohsinon.shared.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
    description = "المعرف الفريد",
    required = true,
    example = "550e8400-e29b-41d4-a716-446655440000"
)
public @interface IdParameter {
}