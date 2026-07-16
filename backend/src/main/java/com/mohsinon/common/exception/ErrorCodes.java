package com.mohsinon.common.exception;

public final class ErrorCodes {

    private ErrorCodes() {
    }

    /*
     * Common
     */
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    /*
     * Users
     */
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    public static final String ROLE_NOT_FOUND = "ROLE_NOT_FOUND";

    /*
     * Mosques
     */
    public static final String MOSQUE_NOT_FOUND = "MOSQUE_NOT_FOUND";
    public static final String MOSQUE_ALREADY_EXISTS = "MOSQUE_ALREADY_EXISTS";
    
    /*
     * Mosques Position
     */
    public static final String MOSQUE_POSITION_NOT_FOUND = "MOSQUE_POSITION_NOT_FOUND";
    public static final String MOSQUE_POSITION_ALREADY_ASSIGNED = "MOSQUE_POSITION_ALREADY_ASSIGNED";
    public static final String POSITION_PERMISSION_ALREADY_EXISTS = "POSITION_PERMISSION_ALREADY_EXISTS";

    /*
     * Mosque Memberships
     */
    public static final String MEMBERSHIP_NOT_FOUND = "MEMBERSHIP_NOT_FOUND";
    public static final String INVALID_MEMBERSHIP_STATUS = "INVALID_MEMBERSHIP_STATUS";
    public static final String MOSQUE_MEMBERSHIP_ALREADY_EXISTS = "MOSQUE_MEMBERSHIP_ALREADY_EXISTS";
    public static final String MOSQUE_MEMBERSHIP_NOT_FOUND = "MOSQUE_MEMBERSHIP_NOT_FOUND";

    /*
     * Donations
     */
    public static final String DONATION_NOT_FOUND = "DONATION_NOT_FOUND";
    public static final String DONATION_CATEGORY_NOT_FOUND = "DONATION_CATEGORY_NOT_FOUND";
    public static final String DONATION_ALREADY_DELIVERED = "DONATION_ALREADY_DELIVERED";

    /*
     * Authorization
     */
    public static final String PERMISSION_NOT_FOUND = "PERMISSION_NOT_FOUND";
    public static final String PERMISSION_GROUP_NOT_FOUND = "PERMISSION_GROUP_NOT_FOUND";
    public static final String POSITION_PERMISSION_NOT_FOUND = "POSITION_PERMISSION_NOT_FOUND";
    public static final String PERMISSION_ALREADY_EXISTS = "PERMISSION_ALREADY_EXISTS";
    public static final String PERMISSION_GROUP_ALREADY_EXISTS = "PERMISSION_GROUP_ALREADY_EXISTS";
    
    public static final String USER_PERMISSION_ALREADY_EXISTS = "USER_PERMISSION_ALREADY_EXISTS";
    public static final String USER_PERMISSION_NOT_FOUND = "USER_PERMISSION_NOT_FOUND";
    

}