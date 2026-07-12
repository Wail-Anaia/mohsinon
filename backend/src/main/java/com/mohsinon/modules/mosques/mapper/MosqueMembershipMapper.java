package com.mohsinon.modules.mosques.mapper;

import com.mohsinon.modules.mosques.dto.response.MosqueMembershipResponse;
import com.mohsinon.modules.mosques.entity.MosqueMembership;

public class MosqueMembershipMapper {

    private MosqueMembershipMapper() {
    }

    public static MosqueMembershipResponse toResponse(MosqueMembership membership) {

        MosqueMembershipResponse response = new MosqueMembershipResponse();

        response.setId(membership.getId());

        response.setMosqueId(membership.getMosque().getId());
        response.setMosqueName(membership.getMosque().getName());

        response.setUserId(membership.getUser().getId());

        response.setFullName(
                membership.getUser().getFirstName()
                        + " "
                        + membership.getUser().getLastName()
        );

        response.setPositionCode(
                membership.getPosition().getCode()
        );

        response.setPositionName(
                membership.getPosition().getName()
        );

        response.setStartDate(
                membership.getStartDate()
        );

        response.setEndDate(
                membership.getEndDate()
        );

        response.setStatus(
        	    membership.getStatus()
        );

        if (membership.getAppointedBy() != null) {

            response.setAppointedById(
                    membership.getAppointedBy().getId()
            );

            response.setAppointedByName(
                    membership.getAppointedBy().getFirstName()
                            + " "
                            + membership.getAppointedBy().getLastName()
            );
        }

        response.setNotes(
                membership.getNotes()
        );

        return response;
    }

}