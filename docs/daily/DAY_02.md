# DAY 02 - بناء نظام المستخدمين والمصادقة (Users & Authentication)

**التاريخ:** 2026-07-08

---

# الهدف من اليوم

كان الهدف من هذا اليوم هو البدء في البرمجة الفعلية لأول وحدة داخل منصة **محسنون**، وذلك ببناء نظام متكامل لإدارة المستخدمين والمصادقة (Authentication).

يمثل هذا النظام حجر الأساس لجميع الوحدات المستقبلية في المنصة، حيث ستعتمد عليه جميع عمليات تسجيل المستخدمين، تسجيل الدخول، إدارة الصلاحيات، وحماية واجهات الـ API.

---

# أهداف اليوم

* إنشاء هيكل Module المستخدمين.
* إنشاء هيكل Module الصلاحيات.
* إنشاء Module المصادقة.
* إنشاء Entities الأساسية.
* إنشاء Repositories.
* إنشاء DTOs.
* إنشاء Services.
* إنشاء Controllers.
* إنشاء نظام Register.
* إنشاء نظام Login.
* تشفير كلمات المرور باستخدام BCrypt.
* إنشاء JWT Token.
* إعداد Spring Security.
* حماية الـ APIs.
* إنشاء Global Exception Handler.
* اختبار جميع العمليات باستخدام Postman.
* إصلاح جميع الأخطاء التي ظهرت أثناء التطوير.

---

# ما تم إنجازه

## 1. تنظيم المشروع

تم إعادة تنظيم المشروع بالكامل ليصبح مبنياً على Modules مستقلة.

أصبح كل جزء من النظام داخل Package مستقلة لتسهيل الصيانة والتطوير مستقبلاً.

---

## 2. إنشاء Module المستخدمين

تم إنشاء:

```
modules/users
```

ويحتوي على:

```
entity
repository
```

---

## 3. إنشاء Module الصلاحيات

تم إنشاء:

```
modules/role
```

ويحتوي على:

```
entity
repository
```

---

## 4. إنشاء Module المصادقة

تم إنشاء:

```
modules/auth
```

ويحتوي على:

```
controller
service
dto
```

---

# هيكل المشروع بعد اليوم الثاني

```
backend
│
├── auth
│   ├── controller
│   ├── dto
│   └── service
│
├── users
│   ├── entity
│   └── repository
│
├── role
│   ├── entity
│   └── repository
│
├── security
│
├── config
│
├── exception
│
└── BackendApplication
```

---

# Backend

تم إنشاء أول طبقات النظام بالكامل.

---

# Entities

## User

تم إنشاء كيان User.

يحتوي على:

* UUID id
* firstName
* lastName
* username
* email
* phone
* password
* enabled
* createdAt
* updatedAt

كما تم إنشاء العلاقة:

```
ManyToMany
```

مع كيان Role.

---

## Role

تم إنشاء كيان Role.

ويحتوي على:

* id
* name

---

# Database

تم إنشاء الجداول التالية تلقائياً بواسطة Hibernate:

```
users

roles

users_roles
```

---

# Repositories

تم إنشاء:

## UserRepository

ويحتوي على:

* findByEmail()
* findByUsername()
* existsByEmail()
* existsByUsername()

---

## RoleRepository

ويحتوي على:

* findByName()

---

# DTOs

تم إنشاء:

## RegisterRequest

يحتوي على بيانات التسجيل.

---

## RegisterResponse

يعيد رسالة نجاح التسجيل.

---

## LoginRequest

يحتوي على:

* email
* password

---

## LoginResponse

كان يعيد في البداية:

```
Login successful
```

ثم تم تطويره لاحقاً ليعيد JWT Token.

---

# Validation

تم إضافة Validation باستخدام:

* @NotBlank
* @Email
* @Size

لجميع الحقول المطلوبة.

---

# AuthService

تم إنشاء جميع منطق المصادقة.

---

## Register

يتحقق من:

* البريد الإلكتروني.
* اسم المستخدم.

ثم:

* تشفير كلمة المرور.
* إسناد الدور USER.
* حفظ المستخدم.

---

## Login

يقوم بـ:

البحث عن المستخدم.

التحقق من كلمة المرور.

إرجاع JWT Token.

---

# Password Encryption

تم استخدام:

```
BCryptPasswordEncoder
```

بدلاً من حفظ كلمات المرور كنص صريح.

---

# Spring Security

تم إنشاء:

```
SecurityConfig
```

وتم:

* تعطيل CSRF.
* جعل النظام Stateless.
* السماح لـ:

```
/api/auth/**
```

بدون تسجيل دخول.

وحماية بقية الـ APIs.

---

# JWT

تم إضافة مكتبة:

```
jjwt
```

وإنشاء:

```
JwtService
```

ويقوم بـ:

* إنشاء Token.
* استخراج Username.
* التحقق من صلاحية Token.

---

تم تعديل Login ليعيد:

```json
{
    "token":"eyJhbGciOi..."
}
```

---

# JwtAuthenticationFilter

تم إنشاء Filter خاص بقراءة الـ Token.

يقوم بـ:

* قراءة Authorization Header.
* استخراج Token.
* التحقق من صحته.
* تحميل المستخدم.
* إنشاء Authentication داخل Spring Security.

---

# UserDetailsService

تم إنشاء:

```
CustomUserDetailsService
```

ليقوم بتحميل المستخدمين من قاعدة البيانات بدلاً من InMemoryUserDetailsManager.

---

# Exception Handling

تم إنشاء Exceptions مخصصة:

* AuthenticationException
* ResourceAlreadyExistsException
* ResourceNotFoundException

---

تم إنشاء:

```
GlobalExceptionHandler
```

لإرجاع رسائل أخطاء موحدة.

---

# قاعدة البيانات

تم ربط المشروع بقاعدة:

```
mohsinon_db
```

واستخدام PostgreSQL.

---

# المشاكل التي واجهتنا

## المشكلة الأولى

لم يتم إنشاء الجداول.

السبب:

```
permission denied for schema public
```

الحل:

تبين أن المستخدم:

```
mohsinon
```

لا يملك صلاحية إنشاء الجداول.

تم استخدام:

```
postgres
```

ثم أنشأ Hibernate جميع الجداول بنجاح.

---

## المشكلة الثانية

ظهور:

```
relation users does not exist
```

وكان السبب فشل إنشاء الجداول.

بعد حل صلاحيات PostgreSQL اختفت المشكلة.

---

## المشكلة الثالثة

ظهور:

```
401 Unauthorized
```

في Postman.

تم تعديل إعدادات Spring Security حتى أصبحت مسارات:

```
/api/auth/register

/api/auth/login
```

متاحة بدون تسجيل دخول.

---

## المشكلة الرابعة

ظهور:

```
Using generated security password
```

وكان Spring Security يستخدم:

```
InMemoryUserDetailsManager
```

تم إنشاء:

```
CustomUserDetailsService
```

واختفت الرسالة نهائياً.

---

## المشكلة الخامسة

ظهور:

```
ConflictingBeanDefinitionException
```

بسبب وجود ملفين:

```
SecurityConfig
```

داخل Packages مختلفة.

تم حذف الملف القديم.

---

# الاختبارات التي تم تنفيذها

## Register

تم إنشاء مستخدم جديد.

النتيجة:

```
200 OK
```

---

## Login

تم تسجيل الدخول.

النتيجة:

```
200 OK
```

وتم إرجاع JWT.

---

## Login بكلمة مرور خاطئة

النتيجة:

```
401 Unauthorized
```

---

## Register ببريد إلكتروني موجود

النتيجة:

```
409 Conflict
```

---

## Validation

تم إرسال بيانات ناقصة.

النتيجة:

```
400 Bad Request
```

---

# التحقق النهائي

تم التأكد من:

* إنشاء الجداول.
* حفظ المستخدم.
* تشفير كلمة المرور.
* تسجيل الدخول.
* إنشاء JWT.
* عمل Spring Security.
* تحميل المستخدم من قاعدة البيانات.
* نجاح جميع اختبارات Postman.

---

# الدروس المستفادة

* أهمية تقسيم المشروع إلى Modules مستقلة.
* عدم حفظ كلمات المرور كنص صريح.
* الاعتماد على JWT بدلاً من Sessions.
* توحيد معالجة الأخطاء.
* اختبار كل خطوة مباشرة باستخدام Postman.
* حل مشاكل PostgreSQL قبل متابعة التطوير.
* بناء النظام بطريقة قابلة للتوسع منذ البداية.

---

# نسبة الإنجاز

| العنصر | الحالة |
|---------|---------|
| Users Module | ✅ |
| Role Module | ✅ |
| Authentication | ✅ |
| Register API | ✅ |
| Login API | ✅ |
| BCrypt | ✅ |
| JWT | ✅ |
| Spring Security | ✅ |
| Validation | ✅ |
| Exception Handler | ✅ |
| PostgreSQL | ✅ |
| Postman Tests | ✅ |

---

# الخطوة القادمة

سيكون اليوم الثالث مخصصاً لبناء أول Module وظيفي داخل المنصة.

سنبدأ بـ:

```
Mosques Module
```

وسيتضمن:

* إنشاء Mosque Entity.
* إنشاء CRUD كامل للمساجد.
* إنشاء DTOs.
* إنشاء Services.
* إنشاء Controllers.
* البحث عن المساجد.
* ربط كل مسجد بمديره.
* تحديد الموقع الجغرافي.
* تجهيز النظام لرفع الصور مستقبلاً.

---

# ملاحظات

يعتبر هذا اليوم أول يوم برمجي فعلي في مشروع **محسنون**.

بنهاية هذا اليوم أصبحت المنصة تمتلك نظام مصادقة احترافي يعتمد على أفضل الممارسات الحديثة في Spring Boot، بما يشمل تشفير كلمات المرور، إدارة المستخدمين، التحقق من صحة البيانات، إنشاء JWT، وحماية واجهات الـ API.

يمثل هذا النظام الأساس الذي ستعتمد عليه جميع الوحدات المستقبلية في المنصة، مثل المساجد، الجمعيات، المتطوعين، المشاريع، والتبرعات.