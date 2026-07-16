package com.mohsinon.modules.mosques.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMosqueRequest {

    @NotBlank(message = "Mosque name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 3000)
    private String description;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "City is required")
    private String city;

    private String district;

    private String address;

    private Double latitude;

    private Double longitude;

    private String phone;

    @Email(message = "Invalid email")
    private String email;

    public CreateMosqueRequest() {
    }    
}