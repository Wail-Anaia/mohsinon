package com.mohsinon.core.pagination;

public enum SortDirection {
    ASC,
    DESC;

    public static SortDirection fromString(String value) {
        if (value == null) return ASC;
        try {
            return SortDirection.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ASC;
        }
    }
}
