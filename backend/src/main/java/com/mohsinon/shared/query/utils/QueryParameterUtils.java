package com.mohsinon.shared.query.utils;

import java.util.HashMap;
import java.util.Map;

public final class QueryParameterUtils {

    private QueryParameterUtils() {
    }

    public static Map<String, String> flatten(
            Map<String, String[]> parameters) {

        Map<String, String> result = new HashMap<>();

        parameters.forEach((key, values) -> {
            if (values != null && values.length > 0) {
                result.put(key, values[0]);
            }
        });

        return result;
    }
}