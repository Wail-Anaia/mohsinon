# DAY 09 - Core Infrastructure Hardening & Lifecycle Management

**التاريخ:** 2026-07-15

---

# نظرة عامة

كان الهدف من اليوم التاسع هو تقوية البنية التحتية (Infrastructure) لمنصة **محسنون** قبل الانتقال إلى توثيق الـ API باستخدام Swagger.

ركز هذا اليوم على بناء طبقة عامة لإدارة دورة حياة الكيانات (Lifecycle)، وتحويل الحذف التقليدي إلى **Soft Delete**، وتحسين Shared Query Layer، وإصلاح جميع المشكلات المتعلقة بالبحث والترتيب والتصفية، حتى أصبحت البنية الأساسية مستقرة وقابلة لإعادة الاستخدام في جميع الوحدات المستقبلية.

---

# أهداف اليوم

- إنشاء Lifecycle Infrastructure عامة.
- دعم Soft Delete على مستوى جميع الكيانات.
- دعم Archive / Restore.
- دعم Activate / Deactivate.
- منع الحذف الفعلي من قاعدة البيانات.
- تحسين Shared Query Layer.
- إصلاح مشاكل Sorting و Filtering.
- إصلاح مشاكل مقارنة LocalDateTime.
- استقرار وحدة المساجد بالكامل.

---

# ما تم إنجازه

---

## أولاً: إنشاء Lifecycle Layer

تم إنشاء حزمة جديدة:

```text
shared
└── lifecycle
```

وتضم:

```text
Activatable.java
Archivable.java
SoftDeletable.java
LifecycleService.java
LifecycleUtils.java
LifecycleException.java
```

---

## ثانياً: إنشاء LifecycleEntity

تم إنشاء:

```text
shared/entity/LifecycleEntity.java
```

ليكون الكلاس الأساسي لجميع الكيانات التي تدعم:

- Active
- Archive
- Soft Delete

ويحتوي على:

```java
active

archived
archivedAt
archivedBy

deleted
deletedAt
deletedBy
```

---

## ثالثاً: إعادة تنظيم طبقات الـ Entities

أصبحت البنية كما يلي:

```text
BaseEntity
        │
        ▼
AuditableEntity
        │
        ▼
LifecycleEntity
        │
        ├──────────────► User
        ├──────────────► Mosque
        ├──────────────► Donation
        ├──────────────► DonationCategory
        └──────────────► بقية الوحدات مستقبلاً
```

---

## رابعاً: إنشاء LifecycleService

تم إنشاء خدمة عامة تدعم:

```java
activate()

deactivate()

archive()

restore()

softDelete()

restoreDeleted()
```

وأصبحت جميع الوحدات تستخدم نفس الخدمة.

---

## خامساً: إنشاء LifecycleUtils

تم نقل جميع العمليات المشتركة إلى:

```text
LifecycleUtils
```

لتجنب تكرار الكود داخل الخدمات المختلفة.

---

# Soft Delete

تم استبدال:

```java
repository.delete(entity);
```

بـ

```java
lifecycleService.softDelete(entity, user);
```

ثم

```java
repository.save(entity);
```

وبذلك لم يعد أي سجل يُحذف فعليًا من قاعدة البيانات.

---

# Restore

تم إضافة دعم استرجاع السجلات المحذوفة.

```java
restoreDeleted()
```

---

# Archive

تم إضافة دعم أرشفة الكيانات.

```java
archive()

restore()
```

---

# Active / Deactivate

تم إنشاء نظام موحد لتفعيل وتعطيل الكيانات.

```java
activate()

deactivate()
```

---

# Shared Query Layer

تم تطوير Shared Query Layer ليصبح يدعم تلقائياً:

- Pagination
- Sorting
- Filtering
- Dynamic Specifications

مع تجاهل السجلات المحذوفة.

```text
deleted = false
```

بدون الحاجة لإضافتها في كل Repository.

---

# إصلاح LocalDateTime Filters

تم إصلاح الخطأ:

```
Cannot compare LocalDateTime with String
```

وأصبح النظام يدعم:

```text
createdAt=>2026-01-01

createdAt=<2027-01-01
```

---

# إصلاح Sorting

تم إصلاح مشكلة:

```
sortBy
```

وأصبحت تعمل بالشكل التالي:

```text
?sortBy=name&direction=asc

?sortBy=name&direction=desc
```

---

# إصلاح Pagination

أصبح النظام يدعم:

```text
?page=0

&size=10
```

مع الفرز والتصفية في نفس الطلب.

---

# إصلاح Multiple Filters

أصبح بالإمكان الجمع بين عدة فلاتر:

```text
country

city

active

createdAt

name
```

داخل نفس الطلب.

---

# تطوير وحدة Mosque

تم إضافة:

- Update Mosque
- Soft Delete Mosque
- Restore Mosque
- Archive Mosque
- Restore Archive
- Activate Mosque
- Deactivate Mosque

---

# الاختبارات المنجزة

## Authentication

✅ Register

✅ Login

---

## Mosque

✅ Create Mosque

✅ Find Mosque By Id

✅ Update Mosque

✅ Soft Delete Mosque

✅ Restore Mosque

---

## Search

✅ Pagination

✅ Sorting

✅ Search By Name

✅ Search By City

✅ Search By Country

✅ Multiple Filters

✅ Greater Than

✅ Less Than

✅ Active Filter

✅ Full Query

---

## Authorization

✅ JWT

✅ Authentication

✅ Permission Validation

---

# المشاكل التي تم حلها

### مشكلة مقارنة LocalDateTime

```
Cannot compare LocalDateTime with String
```

الحل:

تحويل القيم النصية إلى LocalDateTime داخل GenericSpecification.

---

### مشكلة Sorting

```
Could not resolve attribute sortBy
```

الحل:

استبعاد معاملات Pagination وSorting من Dynamic Specification.

---

### مشكلة حذف المسجد

```
Foreign Key Constraint
```

الحل:

استبدال الحذف الحقيقي بـ Soft Delete.

---

### مشكلة Lifecycle

تم إعادة هيكلة الكيانات المشتركة لتصبح أكثر مرونة وقابلية لإعادة الاستخدام.

---

# التحسينات المعمارية

أصبحت جميع الوحدات المستقبلية سترث مباشرة:

```text
LifecycleEntity
```

وبالتالي تحصل تلقائياً على:

- Active
- Archive
- Restore
- Soft Delete
- Restore Deleted
- Auditing
- BaseEntity

بدون كتابة أي كود إضافي.

---

# هيكل البنية الجديد

```text
BaseEntity
        │
        ▼
AuditableEntity
        │
        ▼
LifecycleEntity
        │
        ├── User
        ├── Mosque
        ├── Donation
        ├── DonationCategory
        ├── Volunteer
        ├── Charity
        └── جميع وحدات المنصة
```

---

# الإحصائيات

## Infrastructure

✅ Lifecycle Layer

✅ Shared Query Layer

✅ Soft Delete

✅ Archive

✅ Restore

---

## Mosque Module

✅ مكتمل ومستقر.

---

## Search Engine

✅ مكتمل.

---

## Authorization

✅ مستقر.

---

## Authentication

✅ مستقر.

---

# النتيجة النهائية

بنهاية اليوم التاسع أصبحت منصة **محسنون** تمتلك نواة قوية وقابلة للتوسع، مع بنية موحدة لإدارة دورة حياة الكيانات، ومحرك بحث واستعلام ديناميكي، ودعم كامل للحذف المنطقي والأرشفة، مما يجعل إضافة الوحدات المستقبلية أكثر سرعة واتساقًا واستقرارًا.

---

# نسبة الإنجاز

**Milestone 0.9**

████████████████████████████████████░░ 90%

---

# المرحلة التالية

**DAY 10**

## Swagger / OpenAPI Documentation

- إعداد Swagger/OpenAPI.
- دعم JWT داخل Swagger.
- توثيق جميع الـ APIs.
- تنظيم الـ Controllers باستخدام Tags.
- توثيق DTOs وResponses وExamples.
- تحسين تجربة المطور (Developer Experience) تمهيدًا للانتقال إلى تطوير الوحدات التالية في منصة **محسنون**.