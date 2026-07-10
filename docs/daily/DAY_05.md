# DAY 05 - دمج نظام Authorization مع Spring Security باستخدام AOP

**التاريخ:** 2026-07-10

---

# 🎯 هدف اليوم

الانتقال من نظام التحقق اليدوي من الصلاحيات داخل الخدمات (Services) إلى نظام Authorization احترافي يعتمد على:

- Spring AOP
- Custom Annotations
- Authorization Providers
- Registry Pattern
- Dynamic Permission Resolution

ليصبح النظام قابلاً للتوسع دون تعديل النواة (Core Authorization Engine).

---

# ✅ ما تم إنجازه

## 1. إعادة تصميم AuthorizationService

تم تبسيط AuthorizationService ليصبح مجرد نقطة دخول (Facade) تقوم بتوجيه عملية التحقق إلى الـ Provider المناسب.

بدلاً من احتواء جميع منطق الصلاحيات داخله.

أصبح مسؤولاً فقط عن:

- استقبال بيانات التحقق
- اختيار الـ Provider المناسب
- تمرير الطلب إليه

---

## 2. إنشاء AuthorizationProvider

تم إنشاء الواجهة العامة لجميع مزودي الصلاحيات.

```java
public interface AuthorizationProvider {

    String getGroupCode();

    void checkPermission(
            User user,
            UUID resourceId,
            String permission);

}
```

أصبحت كل وحدة (Module) تمتلك Provider خاصاً بها.

---

## 3. إنشاء AuthorizationRegistry

تم إنشاء Registry يقوم باكتشاف جميع Authorization Providers تلقائياً أثناء تشغيل التطبيق.

يعتمد على Dependency Injection الخاصة بـ Spring.

أصبح تسجيل أي Provider جديد يتم تلقائياً دون تعديل أي كود آخر.

---

## 4. إنشاء أول Provider للمشروع

تم إنشاء:

```
MosqueAuthorizationProvider
```

وأصبح يحتوي جميع منطق صلاحيات المساجد.

ويشمل:

- البحث عن المسجد
- التحقق من User Permissions
- التحقق من عضوية المستخدم
- التحقق من صلاحيات المنصب
- رفض الوصول عند عدم امتلاك الصلاحية

---

## 5. نقل منطق Authorization

تم نقل منطق الصلاحيات من:

```
AuthorizationService
```

إلى:

```
MosqueAuthorizationProvider
```

ليصبح كل Module مسؤولاً عن نفسه.

---

## 6. إنشاء Annotation جديدة

تم إنشاء:

```
@RequirePermission
```

والتي تسمح بحماية أي Method بسهولة.

مثال:

```java
@RequirePermission(
    groupCode = "mosque",
    permission = "mosque.view"
)
```

---

## 7. إنشاء Annotation لاستخراج Resource ID

تم إنشاء:

```
@ResourceId
```

وتستخدم لتحديد أي Parameter يمثل معرف المورد المطلوب التحقق عليه.

مثال:

```java
public void method(
        @ResourceId UUID mosqueId)
```

---

## 8. إنشاء PermissionAspect

تم إنشاء Aspect يعتمد على Spring AOP.

يقوم بالخطوات التالية:

- قراءة Annotation
- استخراج Group Code
- استخراج Permission
- استخراج ResourceId
- الحصول على المستخدم الحالي
- استدعاء AuthorizationService
- تنفيذ العملية في حالة السماح

---

## 9. ربط النظام مع CurrentUserService

أصبح الـ Aspect يحصل على المستخدم الحالي تلقائياً من Spring Security دون الحاجة لتمريره يدوياً.

---

## 10. إنشاء Authorization Test Controller

تم إنشاء Controller مخصص لاختبار النظام.

```http
GET /api/test/{mosqueId}
```

ويستخدم:

```java
@RequirePermission(...)
```

لاختبار النظام بالكامل.

---

# ✅ الاختبارات التي تم تنفيذها

## الاختبار الأول

تم استدعاء Endpoint باستخدام مستخدم يملك الصلاحية.

النتيجة:

```
200 OK
```

---

## الاختبار الثاني

تم التأكد من تنفيذ الـ Aspect.

ظهر في Console:

```
Aspect Executed
```

---

## الاختبار الثالث

تم التأكد من انتقال التنفيذ إلى Provider الصحيح.

ظهر:

```
Mosque Provider
```

---

## الاختبار الرابع

تم التأكد من قراءة المسجد من قاعدة البيانات.

نجح الاستعلام.

---

## الاختبار الخامس

تم التأكد من قراءة User Permissions.

نجح الاستعلام.

---

## الاختبار السادس

تم التأكد من قراءة Mosque Membership.

نجح الاستعلام.

---

## الاختبار السابع

تم الوصول إلى Controller بعد نجاح Authorization.

النتيجة:

```
Authorization Success
```

---

# 🏗️ المعمارية الجديدة

قبل اليوم:

```
Controller
        │
        ▼
Service
        │
        ▼
authorizationService.checkPermission(...)
        │
        ▼
AuthorizationService
```

---

بعد اليوم:

```
Controller
        │
        ▼
@RequirePermission
        │
        ▼
PermissionAspect
        │
        ▼
AuthorizationService
        │
        ▼
AuthorizationRegistry
        │
        ▼
MosqueAuthorizationProvider
        │
        ▼
Repositories
```

---

# ✨ مميزات التصميم الجديد

- فصل كامل بين الوحدات.
- كل Module مسؤول عن صلاحياته.
- سهولة إضافة Modules جديدة.
- عدم تعديل AuthorizationService مستقبلاً.
- تقليل الترابط بين الوحدات.
- دعم Spring AOP.
- دعم Custom Annotations.
- قابلية عالية للتوسع.
- معمارية نظيفة (Clean Architecture).

---

# 📁 الملفات الجديدة

```
security/
│
├── annotation/
│   ├── RequirePermission.java
│   └── ResourceId.java
│
├── aspect/
│   └── PermissionAspect.java
│
├── authorization/
│   ├── AuthorizationProvider.java
│   └── AuthorizationRegistry.java
```

---

```
modules/
│
├── mosques/
│   └── authorization/
│       └── MosqueAuthorizationProvider.java
```

---

```
modules/
│
└── authorization/
    └── service/
        └── AuthorizationService.java
```

---

```
modules/
│
└── test/
    └── AuthorizationTestController.java
```

---

# 🔄 الملفات التي تم تعديلها

- AuthorizationService
- MosqueMembershipService
- MosqueService
- Security Configuration
- pom.xml (إضافة Spring AOP)

---

# 🧪 حالة النظام

| العنصر | الحالة |
|---------|---------|
| JWT Authentication | ✅ |
| Spring Security | ✅ |
| Current User | ✅ |
| Authorization Aspect | ✅ |
| Authorization Registry | ✅ |
| Authorization Provider | ✅ |
| Mosque Provider | ✅ |
| Dynamic Permission Resolution | ✅ |
| Annotation Based Security | ✅ |
| Provider Pattern | ✅ |

---

# 📊 نسبة الإنجاز

| الوحدة | الإنجاز |
|---------|---------:|
| المستخدمون | 100% |
| JWT | 100% |
| المساجد | 100% |
| المناصب | 100% |
| العضويات | 100% |
| الصلاحيات الديناميكية | 100% |
| Authorization Engine | 100% |
| Spring AOP Integration | 100% |

---

# 🎯 مخرجات اليوم

تم الانتهاء من بناء أول نسخة احترافية من محرك الصلاحيات في منصة **محسنون**.

أصبح النظام يعتمد على Spring AOP وAnnotation Based Authorization وProvider Pattern، مما يسمح بإضافة أي وحدة جديدة (الجمعيات، المشاريع، الحملات، التبرعات...) دون الحاجة إلى تعديل محرك الصلاحيات أو إعادة تصميمه.

وبذلك أصبحت منصة محسنون تمتلك بنية صلاحيات معيارية، قابلة للتوسع، وسهلة الصيانة، وتشكل أساساً قوياً لبناء جميع وحدات النظام المستقبلية.