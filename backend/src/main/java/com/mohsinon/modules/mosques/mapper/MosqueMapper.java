package com.mohsinon.modules.mosques.mapper;

import com.mohsinon.modules.mosques.dto.request.CreateMosqueRequest;
import com.mohsinon.modules.mosques.dto.response.MosqueResponse;
import com.mohsinon.modules.mosques.entity.Mosque;

public class MosqueMapper {

    private MosqueMapper() {
    }

    public static Mosque toEntity(CreateMosqueRequest request) {
        Mosque mosque = new Mosque();

        mosque.setName(request.getName());
        mosque.setDescription(request.getDescription());
        mosque.setCountry(request.getCountry());
        mosque.setCity(request.getCity());
        mosque.setDistrict(request.getDistrict());
        mosque.setAddress(request.getAddress());
        mosque.setLatitude(request.getLatitude());
        mosque.setLongitude(request.getLongitude());
        mosque.setPhone(request.getPhone());
        mosque.setEmail(request.getEmail());

        return mosque;
    }

    public static MosqueResponse toResponse(Mosque mosque) {
        MosqueResponse response = new MosqueResponse();

        response.setId(mosque.getId());
        response.setName(mosque.getName());
        response.setDescription(mosque.getDescription());
        response.setCountry(mosque.getCountry());
        response.setCity(mosque.getCity());
        response.setDistrict(mosque.getDistrict());
        response.setAddress(mosque.getAddress());
        response.setLatitude(mosque.getLatitude());
        response.setLongitude(mosque.getLongitude());
        response.setPhone(mosque.getPhone());
        response.setEmail(mosque.getEmail());
        response.setVerified(mosque.getVerified());
        response.setActive(mosque.getActive());
        response.setCreatedAt(mosque.getCreatedAt());
        response.setUpdatedAt(mosque.getUpdatedAt());

        return response;
    }
}