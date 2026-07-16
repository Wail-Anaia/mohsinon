package com.mohsinon.shared.query.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class ValueConverter {

    private ValueConverter() {
    }

    public static Object convert(
            Class<?> targetType,
            Object value) {

        if (value == null) {
            return null;
        }

        String text = value.toString();

        if (targetType.equals(String.class)) {
            return text;
        }

        if (targetType.equals(Boolean.class)
                || targetType.equals(boolean.class)) {

            return Boolean.valueOf(text);
        }

        if (targetType.equals(Integer.class)
                || targetType.equals(int.class)) {

            return Integer.valueOf(text);
        }

        if (targetType.equals(Long.class)
                || targetType.equals(long.class)) {

            return Long.valueOf(text);
        }

        if (targetType.equals(Double.class)
                || targetType.equals(double.class)) {

            return Double.valueOf(text);
        }

        if (targetType.equals(Float.class)
                || targetType.equals(float.class)) {

            return Float.valueOf(text);
        }

        if (targetType.equals(UUID.class)) {

            return UUID.fromString(text);
        }

        if (targetType.equals(LocalDate.class)) {

            return LocalDate.parse(text);
        }

        if (targetType.equals(LocalDateTime.class)) {

            return LocalDate.parse(text).atStartOfDay();
        }

        if (targetType.isEnum()) {

            @SuppressWarnings({ "rawtypes", "unchecked" })
            Enum<?> enumValue =
                    Enum.valueOf(
                            (Class<Enum>) targetType,
                            text.toUpperCase());

            return enumValue;
        }

        return value;
    }

}