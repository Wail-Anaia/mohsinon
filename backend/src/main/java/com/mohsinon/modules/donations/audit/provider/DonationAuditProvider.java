//package com.mohsinon.modules.donations.audit.provider;
//
//import org.springframework.stereotype.Component;
//
//import com.mohsinon.modules.audit.model.AuditAction;
//import com.mohsinon.modules.audit.model.AuditEntityType;
//import com.mohsinon.modules.audit.provider.AuditDescriptionProvider;
//import com.mohsinon.modules.donations.dto.response.DonationResponse;
//
//@Component
//public class DonationAuditProvider
//        implements AuditDescriptionProvider {
//
//    @Override
//    public AuditEntityType getEntityType() {
//        return AuditEntityType.DONATION;
//    }
//
//    @Override
//    public String buildDescription(
//            AuditAction action,
//            Object[] args,
//            Object result) {
//
//        DonationResponse donation = (DonationResponse) result;
//
//        return switch (action) {
//
//            case CREATE ->
//                    "Created donation: " + donation.getTitle();
//
//            case UPDATE ->
//                    "Updated donation: " + donation.getTitle();
//
//            case RECEIVE ->
//                    "Received donation: " + donation.getTitle();
//
//            case ALLOCATE ->
//                    "Allocated donation: " + donation.getTitle();
//
//            case DELIVER ->
//                    "Delivered donation: " + donation.getTitle();
//
//            case CANCEL ->
//                    "Cancelled donation: " + donation.getTitle();
//
//            default ->
//                    "Donation action";
//        };
//    }
//}