package com.mohsinon.core.exception;

import com.mohsinon.core.config.CoreSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {TestErrorController.class, GlobalExceptionHandler.class})
@Import(CoreSecurityConfig.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return 404 ProblemDetail on ResourceNotFoundException")
    void shouldHandleNotFoundException() throws Exception {
        mockMvc.perform(get("/api/v1/test/errors/not-found").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Resource Not Found")))
                .andExpect(jsonPath("$.errorCode", is("RESOURCE_NOT_FOUND")))
                .andExpect(jsonPath("$.detail", is("Mosque with id '00000000-0000-0000-0000-000000000001' was not found.")));
    }

    @Test
    @DisplayName("Should return 409 ProblemDetail on ConflictException")
    void shouldHandleConflictException() throws Exception {
        mockMvc.perform(get("/api/v1/test/errors/conflict").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.errorCode", is("USER_EMAIL_EXISTS")))
                .andExpect(jsonPath("$.detail", is("A user with this email address already exists.")));
    }

    @Test
    @DisplayName("Should return 403 ProblemDetail on ForbiddenException")
    void shouldHandleForbiddenException() throws Exception {
        mockMvc.perform(get("/api/v1/test/errors/forbidden").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.errorCode", is("CANNOT_EDIT_MOSQUE")));
    }

    @Test
    @DisplayName("Should return 400 ProblemDetail with field errors on ValidationException")
    void shouldHandleValidationException() throws Exception {
        mockMvc.perform(get("/api/v1/test/errors/validation").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errorCode", is("VALIDATION_FAILED")))
                .andExpect(jsonPath("$.errors.email", is("Must be a valid email address")));
    }
}
