package com.mohsinon.shared.query.parser;

import java.util.Map;

import com.mohsinon.shared.query.criteria.FilterOperator;
import com.mohsinon.shared.query.request.FilterRequest;

public final class SearchParser {

    private SearchParser() {
    }

    public static FilterRequest parse(
            Map<String, String> parameters) {

        FilterRequest filter = new FilterRequest();

        if (parameters == null) {
            return filter;
        }

        parameters.forEach((key, value) -> {

            if (ignore(key) || value == null || value.isBlank()) {
                return;
            }

            filter.add(
                    key,
                    detectOperation(value),
                    cleanValue(value));
        });

        return filter;
    }

    private static boolean ignore(String key) {

        return key.equals("page")
                || key.equals("size")
                || key.equals("sort")
                || key.equals("sortBy")
                || key.equals("direction")
                || key.equals("search");
    }

    private static FilterOperator detectOperation(String value) {

        if (value.startsWith(">="))
            return FilterOperator.GREATER_THAN_OR_EQUAL;

        if (value.startsWith("<="))
            return FilterOperator.LESS_THAN_OR_EQUAL;

        if (value.startsWith(">"))
            return FilterOperator.GREATER_THAN;

        if (value.startsWith("<"))
            return FilterOperator.LESS_THAN;

        if (value.startsWith("!"))
            return FilterOperator.NOT_EQUAL;

        if (value.startsWith("~"))
            return FilterOperator.LIKE;

        return FilterOperator.EQUAL;
    }

    private static Object cleanValue(String value) {

        if (value.startsWith(">=")
                || value.startsWith("<=")) {

            return value.substring(2);
        }

        if (value.startsWith(">")
                || value.startsWith("<")
                || value.startsWith("!")
                || value.startsWith("~")) {

            return value.substring(1);
        }

        return value;
    }

}