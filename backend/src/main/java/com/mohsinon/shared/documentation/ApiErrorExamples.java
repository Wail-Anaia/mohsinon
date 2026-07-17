package com.mohsinon.shared.documentation;

public final class ApiErrorExamples {

    private ApiErrorExamples() {
    }

    public static final String NOT_FOUND = """
    {
      "status":404,
      "error":"Not Found",
      "message":"Mosque not found"
    }
    """;

    public static final String VALIDATION = """
    {
      "status":400,
      "error":"Validation Error",
      "message":"Validation failed."
    }
    """;
}