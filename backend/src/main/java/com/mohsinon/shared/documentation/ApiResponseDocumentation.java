package com.mohsinon.shared.documentation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

import com.mohsinon.shared.api.ApiErrorResponse;

/**
 * مجموعة من الـ Meta-Annotations
 * لإعادة استخدام توثيق Swagger داخل جميع Controllers.
 */
public final class ApiResponseDocumentation {

    private ApiResponseDocumentation() {
    }

    /**
     * Create
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = SwaggerConstants.CREATED
            ),
            @ApiResponse(
            	    responseCode = "400",
            	    description = SwaggerConstants.VALIDATION_ERROR,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "401",
            	    description = SwaggerConstants.UNAUTHORIZED,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "403",
            	    description = SwaggerConstants.FORBIDDEN,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "409",
            	    description = SwaggerConstants.CONFLICT,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "500",
            	    description = SwaggerConstants.INTERNAL_SERVER_ERROR,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            )
    })
    public @interface CreateApiResponses {
    }

    /**
     * Update
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = SwaggerConstants.UPDATED
            ),
            @ApiResponse(
            	    responseCode = "400",
            	    description = SwaggerConstants.VALIDATION_ERROR,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "401",
            	    description = SwaggerConstants.UNAUTHORIZED,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "403",
            	    description = SwaggerConstants.FORBIDDEN,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "404",
            	    description = SwaggerConstants.NOT_FOUND,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "409",
            	    description = SwaggerConstants.CONFLICT,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "500",
            	    description = SwaggerConstants.INTERNAL_SERVER_ERROR,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            )
    })
    public @interface UpdateApiResponses {
    }

    /**
     * Delete
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = SwaggerConstants.DELETED
            ),
            @ApiResponse(
            	    responseCode = "401",
            	    description = SwaggerConstants.UNAUTHORIZED,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "403",
            	    description = SwaggerConstants.FORBIDDEN,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "404",
            	    description = SwaggerConstants.NOT_FOUND,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "500",
            	    description = SwaggerConstants.INTERNAL_SERVER_ERROR,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            )
    })
    public @interface DeleteApiResponses {
    }

    /**
     * Find By Id
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = SwaggerConstants.SUCCESS
            ),
            @ApiResponse(
            	    responseCode = "401",
            	    description = SwaggerConstants.UNAUTHORIZED,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "403",
            	    description = SwaggerConstants.FORBIDDEN,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "404",
            	    description = SwaggerConstants.NOT_FOUND,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "500",
            	    description = SwaggerConstants.INTERNAL_SERVER_ERROR,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            )
    })
    public @interface GetApiResponses {
    }

    /**
     * Restore / Archive / Activate
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = SwaggerConstants.SUCCESS
            ),
            @ApiResponse(
            	    responseCode = "401",
            	    description = SwaggerConstants.UNAUTHORIZED,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "403",
            	    description = SwaggerConstants.FORBIDDEN,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "404",
            	    description = SwaggerConstants.NOT_FOUND,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            ),
            @ApiResponse(
            	    responseCode = "500",
            	    description = SwaggerConstants.INTERNAL_SERVER_ERROR,
            	    content = @Content(
            	        mediaType = "application/json",
            	        schema = @Schema(implementation = ApiErrorResponse.class)
            	    )
            )
    })
    public @interface LifecycleApiResponses {
    }

}