package com.mohsinon.modules.mosques.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.mohsinon.shared.documentation.ApiExamples;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMosqueRequest {
	
	@Schema(
            description = "اسم المسجد",
            example = ApiExamples.MOSQUE_NAME,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Mosque name is required")
    @Size(max = 255)
    private String name;
	
	@Schema(
            description = "وصف المسجد",
            example = ApiExamples.MOSQUE_DESCRIPTION
    )
    @Size(max = 3000)
    private String description;
	
	@Schema(
            description = "الدولة",
            example = ApiExamples.COUNTRY
    )
    @NotBlank(message = "Country is required")
    private String country;
    
    @Schema(
            description = "المدينة",
            example = ApiExamples.CITY
    )
    @NotBlank(message = "City is required")
    private String city;

    private String district;
    
    @Schema(
            description = "العنوان",
            example = ApiExamples.ADDRESS
    )
    private String address;
    
    @Schema(
            description = "خط العرض",
            example = ApiExamples.LATITUDE
    )
    private Double latitude;
    
    @Schema(
            description = "خط الطول",
            example = ApiExamples.LONGITUDE
    )
    private Double longitude;

    private String phone;

    @Email(message = "Invalid email")
    private String email;

    public CreateMosqueRequest() {
    }    
}