package com.mohsinon.modules.authorization.constants;

public final class PermissionCodes {

    private PermissionCodes() {
    }

    /*
     * ===========================
     * MOSQUE
     * ===========================
     */

    public static final String MOSQUE_VIEW = "mosque.view";
    public static final String MOSQUE_UPDATE = "mosque.update";
    public static final String MOSQUE_ASSIGN_IMAM = "mosque.assign_imam";
    public static final String MOSQUE_ADD_MEMBER = "mosque.add_member";
    public static final String MOSQUE_REMOVE_MEMBER = "mosque.remove_member";
    public static final String MOSQUE_MANAGE_COMMITTEE = "mosque.manage_committee";
    public static final String MOSQUE_MANAGE_DONATIONS = "mosque.manage_donations";
    public static final String MOSQUE_MANAGE_INITIATIVES = "mosque.manage_initiatives";

    /*
     * ===========================
     * DONATION
     * ===========================
     */

    public static final String DONATION_VIEW = "donation.view";
    public static final String DONATION_CREATE = "donation.create";
    public static final String DONATION_UPDATE = "donation.update";
    public static final String DONATION_DELETE = "donation.delete";

    public static final String DONATION_RECEIVE = "donation.receive";
    public static final String DONATION_ALLOCATE = "donation.allocate";
    public static final String DONATION_DELIVER = "donation.deliver";
    public static final String DONATION_CANCEL = "donation.cancel";

    /*
     * ===========================
     * DONATION CATEGORY
     * ===========================
     */

    public static final String DONATION_CATEGORY_VIEW = "donation.category.view";
    public static final String DONATION_CATEGORY_CREATE = "donation.category.create";
    public static final String DONATION_CATEGORY_UPDATE = "donation.category.update";
    public static final String DONATION_CATEGORY_DELETE = "donation.category.delete";

}