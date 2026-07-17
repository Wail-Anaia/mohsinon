package com.mohsinon.modules.mosques.dto.response;

import com.mohsinon.shared.entity.AuditableEntity;

import com.mohsinon.shared.documentation.ApiExamples;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MosqueResponse extends AuditableEntity{
	
	@Schema(
	        description = "اسم المسجد",
	        example = ApiExamples.MOSQUE_NAME
	)
    private String name;
	
    private String description;
    
    private String country;
    
    private String city;
    
    private String district;
    
    private String address;
    
    private Double latitude;
    
    private Double longitude;
    
    private String phone;
    
    private String email;
    
    private Boolean verified;
    
    private Boolean active;

    public MosqueResponse() {
    }   
}