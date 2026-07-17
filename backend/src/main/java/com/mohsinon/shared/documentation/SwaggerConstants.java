package com.mohsinon.shared.documentation;

/**
 * Swagger documentation constants.
 *
 * يحتوي على جميع النصوص المشتركة المستخدمة داخل Swagger
 * لتجنب تكرارها في جميع الـ Controllers.
 */
public final class SwaggerConstants {

    private SwaggerConstants() {
    }

    /*
     * ===========================================
     * Common Responses
     * ===========================================
     */

    public static final String SUCCESS = "تم تنفيذ العملية بنجاح";

    public static final String CREATED = "تم إنشاء المورد بنجاح";

    public static final String UPDATED = "تم تحديث المورد بنجاح";

    public static final String DELETED = "تم حذف المورد بنجاح";

    public static final String RESTORED = "تمت استعادة المورد بنجاح";

    public static final String ARCHIVED = "تمت أرشفة المورد بنجاح";

    public static final String ACTIVATED = "تم تفعيل المورد بنجاح";

    public static final String DEACTIVATED = "تم تعطيل المورد بنجاح";

    /*
     * ===========================================
     * Error Responses
     * ===========================================
     */

    public static final String VALIDATION_ERROR = "بيانات الإدخال غير صحيحة";

    public static final String UNAUTHORIZED ="المستخدم غير مصادق";

    public static final String FORBIDDEN ="ليس لديك الصلاحية لتنفيذ هذه العملية";

    public static final String NOT_FOUND = "المورد غير موجود";

    public static final String CONFLICT ="تعارض في البيانات";

    public static final String INTERNAL_SERVER_ERROR = "حدث خطأ غير متوقع داخل الخادم";

    /*
     * ===========================================
     * UUID Parameter
     * ===========================================
     */

    public static final String UUID_DESCRIPTION = "المعرف الفريد (UUID)";

    public static final String UUID_EXAMPLE = "550e8400-e29b-41d4-a716-446655440000";
}