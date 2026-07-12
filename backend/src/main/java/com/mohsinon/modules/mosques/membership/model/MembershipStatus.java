package com.mohsinon.modules.mosques.membership.model;

public enum MembershipStatus {

    /**
     * تمت دعوة المستخدم ولم يقبل بعد.
     */
    INVITED,

    /**
     * بانتظار المراجعة أو الموافقة.
     */
    PENDING,

    /**
     * عضو فعال.
     */
    ACTIVE,

    /**
     * العضوية معلقة مؤقتاً.
     */
    SUSPENDED,

    /**
     * تم رفض الدعوة.
     */
    REJECTED,

    /**
     * انتهت العضوية.
     */
    TERMINATED

}