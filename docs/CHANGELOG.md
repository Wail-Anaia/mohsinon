جميع التغييرات المهمة في مشروع **Mohsinon** سيتم توثيقها في هذا الملف.

يعتمد هذا المشروع على مبدأ **Keep a Changelog** مع توثيق الإنجازات حسب يوم التطوير.

---
#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 1 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# [0.1.0] - 2026-07-07

## 🎉 البداية الرسمية لمشروع محسنون

يمثل هذا اليوم البداية الرسمية لتطوير منصة **Mohsinon**.

تم التركيز بالكامل على تأسيس المشروع وتجهيز بيئة العمل دون كتابة أي منطق برمجي.

---

## Added

### Git & GitHub

* إنشاء مستودع GitHub باسم **mohsinon**.
* تهيئة Git محليًا.
* ربط المشروع بالمستودع البعيد.
* إنشاء أول Commit.
* رفع المشروع إلى GitHub.

---

### Backend

تم إنشاء مشروع Backend باستخدام:

* Java 21
* Spring Boot
* Maven

تمت إضافة الاعتماديات التالية:

* Spring Web
* Spring Security
* Spring Data JPA
* Validation
* PostgreSQL Driver
* Lombok
* Spring Boot DevTools

---

### Frontend

تم إنشاء مشروع Angular باستخدام:

* Standalone Components
* Angular Router
* SCSS

كما تمت إضافة:

* Angular Material

وتم التأكد من تشغيل المشروع بنجاح.

---

### Database

تم إنشاء قاعدة بيانات PostgreSQL:

```text
mohsinon_db
```

---

### Documentation

تم إنشاء مجلد التوثيق:

```text
docs/
```

وتمت إضافة الملفات الأساسية:

* VISION.md
* ROADMAP.md
* REQUIREMENTS.md

---

## Changed

### Repository Structure

تم اعتماد الهيكل الأساسي التالي للمشروع:

```text
mohsinon/
│
├── backend/
├── frontend/
├── docs/
├── README.md
└── .gitignore
```

---

## Fixed

### Git Repository

تم إصلاح مشكلة إنشاء Git Repository داخل مجلد Frontend.

تم تحويل Frontend من Submodule إلى مجلد عادي داخل المشروع.

---

### Eclipse Workspace

تم فصل المشروع عن Workspace الخاص بـ Eclipse.

تم نقل المشروع إلى مجلد مستقل لضمان عدم رفع ملفات Eclipse إلى GitHub.

---

### Repository Cleanup

تم تنظيف المستودع من الملفات غير الضرورية.

تم التأكد من عدم تتبع:

* .metadata
* node_modules
* .angular
* target
* ملفات IDE

---

## Security

لا يوجد أي إعداد أمني في هذه المرحلة.

سيتم تنفيذ نظام المصادقة في الأيام القادمة.

---

## Database

لا توجد أي جداول في قاعدة البيانات حتى الآن.

---

## API

لم يتم إنشاء أي REST API.

---

## Testing

لم يتم إنشاء أي اختبارات.

---

## Documentation

تم اعتماد استراتيجية توثيق المشروع.

سيتم إنشاء:

* تقرير يومي بعد نهاية كل يوم تطوير.
* تحديث PROJECT_STATUS.md.
* تحديث هذا الملف مع كل إنجاز جديد.

---

## Notes

كان الهدف من هذا اليوم هو تجهيز المشروع بالكامل قبل البدء في كتابة الكود.

وقد تم الالتزام بعدم كتابة أي Business Logic حتى تصبح بيئة العمل مستقرة ونظيفة.

---

# الإصدارات القادمة

## الإصدار 0.2.0

سيشمل:

* Users Module
* Roles
* Authentication
* JWT
* Refresh Token
* Validation
* Exception Handling
* أول REST APIs

---

## قواعد تحديث هذا الملف

يجب تحديث هذا الملف عند حدوث أي تغيير مهم في المشروع، مثل:

* إضافة ميزة جديدة.
* تعديل معمارية النظام.
* إصلاح مشكلة.
* تحسين الأداء.
* تحديث المكتبات.
* تغيير هيكل المشروع.
* إضافة وحدة جديدة.

---

> **ملاحظة:** هذا الملف يمثل السجل التاريخي الكامل للمشروع، لذلك لا يتم حذف أي إدخال سابق. تتم إضافة الإنجازات الجديدة فقط مع الاحتفاظ بجميع السجلات السابقة.


#### ##### #### ##### #### ##### #### ##### #### #####
### Day 2 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# CHANGELOG.md

جميع التغييرات المهمة في مشروع **منصة محسنون** سيتم توثيقها في هذا الملف.

يعتمد هذا المشروع على مبدأ **Keep a Changelog** مع اتباع **Semantic Versioning**.

---

# [0.2.0] - 2026-07-08

## اليوم الثاني - نظام المستخدمين والمصادقة (Users & Authentication)

### تمت الإضافة (Added)

#### نظام المستخدمين

- إنشاء User Entity.
- إنشاء Role Entity.
- إنشاء العلاقة Many-to-Many بين المستخدمين والأدوار.
- استخدام UUID كمفتاح أساسي للمستخدم.
- إضافة الحقول الأساسية:
  - First Name
  - Last Name
  - Username
  - Email
  - Phone
  - Password
  - Enabled
  - Created At
  - Updated At

---

#### قاعدة البيانات

- إنشاء جدول:

```
users
```

- إنشاء جدول:

```
roles
```

- إنشاء جدول:

```
users_roles
```

- إنشاء جميع القيود (Constraints).
- إنشاء العلاقات (Foreign Keys).

---

#### Repositories

إضافة:

- UserRepository
- RoleRepository

مع الدوال التالية:

- findByEmail()
- findByUsername()
- existsByEmail()
- existsByUsername()
- findByName()

---

#### Authentication

إضافة:

```
POST /api/auth/register
```

ويقوم بـ:

- التحقق من صحة البيانات.
- التحقق من البريد الإلكتروني.
- التحقق من اسم المستخدم.
- تشفير كلمة المرور.
- إسناد دور USER.
- حفظ المستخدم.

---

إضافة:

```
POST /api/auth/login
```

ويقوم بـ:

- التحقق من البريد الإلكتروني.
- التحقق من كلمة المرور.
- إنشاء JWT Token.
- إرجاع Token للمستخدم.

---

#### JWT

إضافة:

- JwtService
- إنشاء Secret Key
- إنشاء Access Token
- التحقق من صحة Token
- استخراج Username من Token
- تحديد مدة صلاحية Token

---

#### Spring Security

إضافة:

- SecurityConfig
- PasswordEncoder
- AuthenticationManager
- Stateless Session Management
- السماح بمسارات:
  - /api/auth/register
  - /api/auth/login
- حماية جميع المسارات الأخرى.

---

#### Validation

إضافة Validation لجميع DTOs باستخدام:

- @NotBlank
- @Email
- @Size

---

#### Exception Handling

إضافة:

- GlobalExceptionHandler
- AuthenticationException
- ResourceAlreadyExistsException
- ResourceNotFoundException

مع استجابات JSON موحدة.

---

### تم التغيير (Changed)

- تحديث المشروع من Spring Boot 4.1.0 إلى Spring Boot 3.5.x لضمان الاستقرار والتوافق مع النظام البيئي الحالي.
- تحسين تنظيم الحزم (Packages) وفق أسلوب Modular Architecture.
- فصل طبقات التطبيق إلى:
  - Controller
  - Service
  - Repository
  - Entity
  - DTO
  - Config
  - Exception

---

### تم الإصلاح (Fixed)

#### PostgreSQL

حل مشكلة:

```
permission denied for schema public
```

عن طريق استخدام مستخدم PostgreSQL يمتلك صلاحيات إنشاء الجداول.

---

#### Hibernate

إصلاح مشكلة عدم إنشاء الجداول تلقائيًا.

---

#### Spring Security

حل مشكلة:

```
401 Unauthorized
```

بإعداد SecurityConfig بشكل صحيح.

---

#### JWT

إصلاح إنشاء الـ Token وربطه بعملية تسجيل الدخول.

---

#### Bean Definition

إصلاح تعارض ملفي:

```
SecurityConfig
```

والاحتفاظ بملف إعداد واحد فقط.

---

#### Validation

إصلاح استجابات الأخطاء الخاصة بالحقول المطلوبة وإرجاع رسائل واضحة.

---

### الاختبارات (Tests)

تم اختبار بنجاح:

- إنشاء مستخدم جديد.
- منع تكرار البريد الإلكتروني.
- منع تكرار اسم المستخدم.
- تسجيل الدخول الصحيح.
- رفض كلمة المرور الخاطئة.
- تشفير كلمات المرور باستخدام BCrypt.
- إنشاء JWT Token.
- التحقق من صحة JWT.
- إنشاء الجداول في PostgreSQL.
- تشغيل المشروع دون أخطاء.

---

### النتيجة

أصبح المشروع يمتلك نظام مصادقة متكامل يعتمد على:

- Spring Security
- JWT Authentication
- BCrypt Password Encryption
- Validation
- Global Exception Handling

وأصبح جاهزًا للانتقال إلى بناء أول وحدة أعمال في المنصة.

---

# [0.1.0] - 2026-07-07

## اليوم الأول - تأسيس المشروع

### تمت الإضافة (Added)

- إنشاء مستودع GitHub.
- إنشاء مشروع Backend باستخدام Spring Boot.
- إنشاء مشروع Frontend باستخدام Angular.
- إنشاء قاعدة بيانات PostgreSQL.
- تنظيم هيكل المشروع.
- إعداد Git وGitHub.
- إعداد ملفات التوثيق داخل مجلد docs.
- تجهيز بيئة التطوير بالكامل.

---

### النتيجة

تم تأسيس مشروع **منصة محسنون** وفق معايير احترافية، ليكون جاهزًا لبدء التطوير الفعلي في الأيام التالية.

#### ##### #### ##### #### ##### #### ##### #### #####
### Day 3 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# CHANGELOG

جميع التغييرات المهمة في مشروع **منصة محسنون (Mohsinon Platform)** سيتم توثيقها في هذا الملف.

يعتمد هذا المشروع على مبادئ **Keep a Changelog** مع استخدام **Semantic Versioning**.

---

# [v0.0.3-alpha] - 2026-07-09

## 🎉 اليوم الثالث - بناء وحدة المساجد (Mosque Module)

يمثل هذا الإصدار أول إضافة حقيقية لوحدة أعمال (Business Module) داخل المنصة، حيث أصبحت المنصة قادرة على إدارة بيانات المساجد من خلال واجهات REST API مع اعتماد بنية معمارية احترافية قابلة للتوسع.

---

## ✨ تمت الإضافة (Added)

### Mosque Module

- إنشاء وحدة `modules/mosque`.
- إنشاء `Mosque` Entity.
- إنشاء `MosqueRepository`.
- إنشاء `MosqueService`.
- إنشاء `MosqueController`.

### REST APIs

إضافة واجهات REST التالية:

- إنشاء مسجد جديد.
- عرض جميع المساجد.
- عرض تفاصيل مسجد بواسطة UUID.

### DTO Layer

إضافة:

- `CreateMosqueRequest`
- `MosqueResponse`

لفصل واجهات الـ API عن كيانات قاعدة البيانات.

### Mapper Layer

إضافة:

- `MosqueMapper`

للتحويل بين:

- Request DTO
- Entity
- Response DTO

### Validation

إضافة التحقق من صحة البيانات باستخدام:

- `@Valid`
- `@NotBlank`
- `@Email`
- `@Size`

### Exception Handling

إضافة:

- `MosqueNotFoundException`

وربطها مع نظام الاستثناءات العام للمشروع.

---

## 🏗️ تحسينات معمارية (Architecture)

إعادة تنظيم المشروع بالكامل إلى:

```text
com.mohsinon
│
├── common
├── config
├── exception
├── security
└── modules
```

ونقل جميع الوحدات الحالية إلى:

```text
modules/
```

بما في ذلك:

- User Module
- Mosque Module

---

## 🗄️ قاعدة البيانات

إضافة جدول:

```text
mosques
```

بجميع الحقول الأساسية:

- UUID
- معلومات الموقع
- بيانات الاتصال
- حالة التفعيل
- حالة التحقق
- تواريخ الإنشاء والتحديث

---

## 🔄 تحسينات (Changed)

### Mosque Service

تم التحويل من استخدام:

```java
Mosque
```

إلى:

```java
CreateMosqueRequest

MosqueResponse
```

---

### Mosque Controller

أصبح يعتمد بالكامل على DTO بدلاً من التعامل المباشر مع Entity.

---

### Exception System

تم دمج وحدة المساجد مع نظام الاستثناءات المركزي للمشروع.

---

### Project Structure

إعادة تنظيم جميع Packages لتتبع أسلوب Modular Architecture.

---

## 🧪 الاختبارات (Tested)

تم اختبار:

- إنشاء مسجد.
- عرض جميع المساجد.
- عرض مسجد واحد.
- Bean Validation.
- DTO Mapping.
- الاتصال بقاعدة البيانات.
- إنشاء الجداول.
- تشغيل المشروع بعد إعادة الهيكلة.

جميع الاختبارات نجحت بنجاح.

---

## 🛠️ إصلاحات (Fixed)

- إصلاح تعارض `GlobalExceptionHandler`.
- توحيد نظام معالجة الاستثناءات.
- تحديث جميع Package Declarations بعد إعادة الهيكلة.
- تحديث جميع Import Statements.
- معالجة أخطاء ناتجة عن نقل الملفات إلى `modules`.

---

## 📁 ملفات جديدة

```text
modules/mosque/entity/Mosque.java

modules/mosque/repository/MosqueRepository.java

modules/mosque/service/MosqueService.java

modules/mosque/controller/MosqueController.java

modules/mosque/dto/request/CreateMosqueRequest.java

modules/mosque/dto/response/MosqueResponse.java

modules/mosque/mapper/MosqueMapper.java

exception/MosqueNotFoundException.java
```

---

## ✏️ ملفات تم تعديلها

```text
GlobalExceptionHandler.java

MosqueService.java

MosqueController.java

pom.xml
```

---

# [v0.0.2-alpha] - 2026-07-08

## 👥 اليوم الثاني - نظام المستخدمين (User Module)

### ✨ تمت الإضافة

- إنشاء `User Entity`.
- إنشاء `Role Entity`.
- إنشاء `UserRepository`.
- إنشاء `RoleRepository`.
- إنشاء نظام تسجيل المستخدمين.
- إنشاء نظام تسجيل الدخول.
- تشفير كلمات المرور باستخدام BCrypt.
- إعداد JWT للمصادقة.
- إنشاء REST APIs الخاصة بالمستخدمين.
- اختبار جميع الواجهات باستخدام Postman.

### 🗄️ قاعدة البيانات

إضافة الجداول:

- `users`
- `roles`
- `users_roles`

### 🧪 الاختبارات

- تسجيل مستخدم جديد.
- تسجيل الدخول.
- التحقق من تشفير كلمات المرور.
- اختبار الاتصال بقاعدة البيانات.

---

# [v0.0.1-alpha] - 2026-07-07

## 🚀 اليوم الأول - تأسيس المشروع

### ✨ تمت الإضافة

- إنشاء مستودع GitHub.
- إنشاء مشروع Spring Boot.
- إنشاء مشروع Angular.
- إعداد Maven.
- إعداد PostgreSQL.
- إعداد هيكل المشروع.
- تشغيل أول نسخة من النظام.

### 📚 التوثيق

إضافة ملفات التوثيق الأساسية:

- VISION.md
- ROADMAP.md
- ARCHITECTURE.md
- REQUIREMENTS.md
- DECISIONS.md
- PROJECT_STATUS.md
- CHANGELOG.md

---

# ملخص الإصدارات

| الإصدار | التاريخ | الوصف |
|----------|----------|-------|
| v0.0.3-alpha | 2026-07-09 | بناء وحدة المساجد وإعادة هيكلة المشروع |
| v0.0.2-alpha | 2026-07-08 | بناء نظام المستخدمين والمصادقة |
| v0.0.1-alpha | 2026-07-07 | تأسيس المشروع وإعداد البيئة |

#### ##### #### ##### #### ##### #### ##### #### #####
### Day 4 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# CHANGELOG

جميع التغييرات المهمة في مشروع **منصة محسنون (Mohsinon Platform)** سيتم توثيقها في هذا الملف.

يعتمد هذا الملف على مبادئ **Keep a Changelog** مع استخدام **Semantic Versioning**.

---

# [0.4.0] - 2026-07-09

## 🎯 DAY 04 — نظام العضويات والصلاحيات الديناميكية

يمثل هذا الإصدار نقطة تحول معمارية في المشروع، حيث تم الانتقال من CRUD تقليدي إلى نظام إدارة مؤسسي يعتمد على المناصب والصلاحيات الديناميكية.

---

## ✨ Added

### إدارة المناصب (Mosque Positions)

- إنشاء كيان `MosquePosition`.
- إضافة حقل `uniquePosition`.
- دعم المناصب الفريدة داخل المسجد.
- إنشاء `MosquePositionRepository`.
- إنشاء `MosquePositionSeeder`.
- إضافة المناصب الأساسية:
  - IMAM
  - COMMITTEE_PRESIDENT
  - COMMITTEE_VICE_PRESIDENT
  - COMMITTEE_SECRETARY
  - COMMITTEE_TREASURER
  - COMMITTEE_MEMBER

---

### إدارة العضويات (Mosque Memberships)

إضافة كيان جديد:

- MosqueMembership

يشمل:

- المسجد
- المستخدم
- المنصب
- تاريخ البداية
- تاريخ النهاية
- الحالة
- من قام بالتعيين
- الملاحظات

---

### Repository

إضافة:

- MosqueMembershipRepository

مع استعلامات مخصصة مثل:

- البحث عن الأعضاء النشطين.
- البحث عن الإمام الحالي.
- البحث عن تاريخ المستخدم.
- البحث عن تاريخ الأئمة.
- التحقق من المناصب الفريدة.
- التحقق من العضويات النشطة.

---

### DTOs

إضافة:

- AssignMembershipRequest
- MosqueMembershipResponse

---

### Mapper

إضافة:

- MosqueMembershipMapper

---

### Service

إضافة:

- MosqueMembershipService

يتضمن:

- إضافة عضو.
- تعيين منصب.
- تغيير الإمام.
- إنهاء العضوية.
- عرض أعضاء المسجد.
- عرض الإمام الحالي.
- عرض تاريخ الأئمة.
- عرض تاريخ المستخدم.

---

### Business Rules

إضافة قواعد العمل التالية:

- منع امتلاك المستخدم لأكثر من عضوية نشطة داخل نفس المسجد.
- منع تكرار المناصب الفريدة.
- إنهاء عضوية الإمام القديم تلقائياً عند تعيين إمام جديد.
- الاحتفاظ بالسجل التاريخي الكامل.

---

## 🔐 Authorization

إضافة محرك صلاحيات ديناميكي جديد.

---

### كيانات جديدة

- PermissionGroup
- Permission
- PositionPermission
- UserPermission

---

### Repositories

إضافة:

- PermissionRepository
- PermissionGroupRepository
- PositionPermissionRepository
- UserPermissionRepository

---

### Seeders

إضافة:

- PermissionGroupSeeder
- PermissionSeeder
- PositionPermissionSeeder

---

### AuthorizationService

إضافة خدمة جديدة للتحقق من الصلاحيات.

تعتمد على:

1. User Permissions
2. Membership
3. Position
4. Position Permissions

---

### صلاحيات المسجد

إضافة أول مجموعة من الصلاحيات:

- mosque.view
- mosque.update
- mosque.assign_imam
- mosque.add_member
- mosque.remove_member
- mosque.manage_committee
- mosque.manage_donations
- mosque.manage_initiatives

---

### مجموعات الصلاحيات

إضافة:

- MOSQUE
- ASSOCIATION
- PROJECT
- DONATION
- VOLUNTEER
- ADMIN

---

## ♻️ Refactored

إعادة هيكلة `MosqueMembershipService`.

إضافة دوال خاصة:

- getPositionByCode()
- validateUserMembership()
- validateUniquePosition()
- createMembership()

لتقليل تكرار الكود وتحسين قابلية الصيانة.

---

## 🛠 Improved

- تحسين تصميم العلاقات بين المستخدمين والمناصب.
- تحسين بنية الخدمات.
- تحسين تصميم الصلاحيات.
- تحسين Seeder للمناصب.
- تحسين Seeder للصلاحيات.
- تحسين تنظيم الحزم (Modules).

---

## 🐞 Fixed

- إصلاح مشكلة تكرار الإمام.
- إصلاح التحقق من العضويات النشطة.
- إصلاح التحقق من المناصب الفريدة.
- إصلاح أنواع معرفات (ID) في بعض الـ Repositories.
- تحسين استعلامات JPA الخاصة بالعضويات.

---

# [0.3.0] - 2026-07-08

## 🎯 DAY 03 — وحدة المساجد

### Added

- إنشاء Mosque Entity.
- إنشاء Mosque Repository.
- إنشاء Mosque Service.
- إنشاء Mosque Controller.
- إنشاء REST APIs الخاصة بالمساجد.
- دعم CRUD للمساجد.

---

# [0.2.0] - 2026-07-07

## 🎯 DAY 02 — المستخدمون والمصادقة

### Added

- User Entity.
- Role Entity.
- UserRepository.
- RoleRepository.
- AuthenticationService.
- JWT Authentication.
- BCrypt Password Encoder.
- Spring Security.
- Register API.
- Login API.

---

# [0.1.0] - 2026-07-06

## 🎯 DAY 01 — تأسيس المشروع

### Added

- إنشاء مستودع Git.
- إنشاء مشروع Spring Boot.
- إنشاء مشروع Angular.
- إعداد PostgreSQL.
- إعداد Maven.
- إعداد هيكل المشروع.
- إنشاء مجلد docs.
- إنشاء الهيكل المعماري الأول للمشروع.
- إعداد بيئة التطوير.

---

# إحصائيات الإصدار الحالي (0.4.0)

## كيانات (Entities)

- User
- Role
- Mosque
- MosqueMembership
- MosquePosition
- Permission
- PermissionGroup
- PositionPermission
- UserPermission

**الإجمالي: 9**

---

## Services

- AuthenticationService
- JwtService
- MosqueService
- MosqueMembershipService
- AuthorizationService

**الإجمالي: 5**

---

## Repositories

- UserRepository
- RoleRepository
- MosqueRepository
- MosqueMembershipRepository
- MosquePositionRepository
- PermissionRepository
- PermissionGroupRepository
- PositionPermissionRepository
- UserPermissionRepository

**الإجمالي: 9**

---

## Seeders

- MosquePositionSeeder
- PermissionGroupSeeder
- PermissionSeeder
- PositionPermissionSeeder

**الإجمالي: 4**

---

**الحالة الحالية:** 🟢 مستقرة

**الإصدار القادم:** **0.5.0 — Authorization Framework (Spring AOP + @RequirePermission)**

#### ##### #### ##### #### ##### #### ##### #### #####
### Day 5 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# CHANGELOG

جميع التغييرات المهمة في مشروع **منصة محسنون (Mohsinon Platform)** سيتم توثيقها في هذا الملف.

يعتمد هذا المشروع على مبادئ **Keep a Changelog** مع الالتزام بالإصدار الدلالي (Semantic Versioning).

---

# [0.5.0-alpha] - 2026-07-10

## 🚀 أكبر تحديث معماري منذ بداية المشروع

تمت إعادة تصميم نظام الصلاحيات بالكامل وتحويله إلى محرك Authorization احترافي يعتمد على Spring AOP وProvider Pattern، مما يجعل النظام قابلاً للتوسع دون تعديل النواة.

---

## ✨ Added

### Authorization Engine

- إنشاء AuthorizationProvider Interface.
- إنشاء AuthorizationRegistry.
- دعم التسجيل التلقائي لجميع Providers.
- إنشاء أول MosqueAuthorizationProvider.
- فصل منطق الصلاحيات عن AuthorizationService.

---

### Spring AOP

- إضافة Spring AOP للمشروع.
- إنشاء PermissionAspect.
- تنفيذ Authorization قبل تنفيذ أي Method محمية.

---

### Security Annotations

إضافة Annotation جديدة:

- `@RequirePermission`
- `@ResourceId`

وأصبح بالإمكان حماية أي Method بواسطة Annotation فقط.

---

### Dynamic Authorization

إضافة نظام يعتمد على:

- Permission Groups
- Authorization Registry
- Authorization Providers

بدلاً من الاعتماد على شروط if/else داخل AuthorizationService.

---

### Testing

إضافة AuthorizationTestController لاختبار:

- Aspect
- Registry
- Provider
- AuthorizationService

---

## 🔄 Changed

### AuthorizationService

قبل:

كان يحتوي جميع منطق الصلاحيات.

بعد:

أصبح مجرد Facade يقوم بتوجيه الطلب إلى AuthorizationProvider المناسب.

---

### Mosque Authorization

تم نقل جميع منطق صلاحيات المساجد إلى:

```
MosqueAuthorizationProvider
```

وأصبح مسؤولاً عن:

- البحث عن المسجد
- التحقق من User Permissions
- التحقق من عضوية المستخدم
- التحقق من صلاحيات المنصب

---

### Security Architecture

تم الانتقال من:

```
Controller
      │
      ▼
Service
      │
      ▼
authorizationService.checkPermission()
```

إلى:

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
AuthorizationProvider
```

---

## 🏗 Architecture

تم اعتماد الأنماط التالية:

- Provider Pattern
- Registry Pattern
- Annotation Driven Authorization
- Aspect Oriented Programming (AOP)
- Dynamic Permission Resolution

---

## 🧪 Tested

تم اختبار:

- PermissionAspect
- AuthorizationRegistry
- MosqueAuthorizationProvider
- CurrentUserService
- JWT Authentication
- Spring Security Integration

---

تم التأكد من نجاح السيناريوهات التالية:

- المستخدم يملك الصلاحية → ✅ 200 OK
- المستخدم عضو في المسجد → ✅
- انتقال التنفيذ عبر Aspect → ✅
- انتقال التنفيذ إلى Provider الصحيح → ✅
- تنفيذ Authorization قبل Controller → ✅

---

## 📁 New Files

### Security

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

### Authorization

```
modules/
│
└── authorization/
    └── service/
        └── AuthorizationService.java
```

---

### Mosques

```
modules/
│
└── mosques/
    └── authorization/
        └── MosqueAuthorizationProvider.java
```

---

### Testing

```
modules/
│
└── test/
    └── AuthorizationTestController.java
```

---

## 🛠 Updated Files

- AuthorizationService
- MosqueMembershipService
- MosqueService
- pom.xml
- Security Configuration

---

## 📦 Dependencies

تمت إضافة:

```
spring-boot-starter-aop
```

لدعم Spring AOP.

---

## 🔐 Security

أصبح المشروع يدعم:

- JWT Authentication
- Spring Security
- Current User Resolution
- Dynamic Permissions
- User Permissions
- Position Permissions
- Provider Based Authorization
- Annotation Based Authorization

---

## 📊 Progress

بعد هذا التحديث أصبحت الوحدات التالية مكتملة بالكامل:

- Users
- JWT Authentication
- Mosques
- Mosque Positions
- Mosque Memberships
- Permission Groups
- Permissions
- Position Permissions
- User Permissions
- Authorization Engine
- Spring AOP Integration

---

# [0.4.0-alpha] - 2026-07-09

## Added

- Mosque Positions
- Mosque Memberships
- Imam Management
- Membership History
- Permission Groups
- Permissions
- Position Permissions
- User Permissions

---

# [0.3.0-alpha] - 2026-07-08

## Added

- Mosque Module
- Mosque CRUD
- Mosque Mapper
- Mosque DTOs
- Mosque Repository
- Mosque Service
- Global Exception Handling

---

# [0.2.0-alpha] - 2026-07-07

## Added

- User Management
- Roles
- Registration
- Login
- BCrypt Password Encoding
- JWT Authentication
- Spring Security
- Current User

---

# [0.1.0-alpha] - 2026-07-07

## Initial Release

### Added

- إنشاء مستودع GitHub
- إنشاء Backend باستخدام Spring Boot
- إنشاء Frontend باستخدام Angular
- إعداد PostgreSQL
- إنشاء الهيكل العام للمشروع
- إنشاء بنية المجلدات
- إعداد Maven
- إعداد Git
- إعداد وثائق المشروع

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 6 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# CHANGELOG

جميع التغييرات المهمة في مشروع **منصة محسنون (Mohsinon Platform)** سيتم توثيقها في هذا الملف.

يعتمد هذا الملف على مبادئ **Keep a Changelog** مع اتباع **Semantic Versioning** مستقبلاً.

---

# [Unreleased]

## Planned

### Frontend
- إنشاء واجهة Angular.
- لوحة التحكم (Dashboard).
- نظام تسجيل الدخول.
- إدارة المستخدمين.
- إدارة المساجد.
- إدارة التبرعات.
- إدارة المبادرات.
- إدارة الجمعيات.

### Backend
- Donations Module.
- Associations Module.
- Projects Module.
- Volunteer Module.
- Marketplace Module.
- Notification Module.
- AI Module.

---

# [0.6.0] - 2026-07-12

## Added

### Authorization Engine

تم إنشاء نظام صلاحيات ديناميكي متكامل.

يشمل:

- Permission Groups
- Permissions
- Position Permissions
- User Permissions

---

### Permission Resolution Layer

تم إنشاء:

- PermissionResolver
- DirectPermissionResolver
- PositionPermissionResolver
- CompositePermissionResolver

لاستخراج جميع صلاحيات المستخدم من مصادر متعددة داخل طبقة موحدة.

---

### Authorization Layer

تم تطوير:

- AuthorizationProvider
- AuthorizationRegistry
- AuthorizationService

لإدارة صلاحيات الوحدات المختلفة بطريقة قابلة للتوسع.

---

### Permission Cache

تم إنشاء نظام Cache لتخزين الصلاحيات المحسوبة بهدف تحسين الأداء وتقليل الاستعلامات المتكررة.

يشمل:

- Cache Put
- Cache Get
- Cache Evict
- Cache Clear

---

### REST APIs

تم إنشاء واجهات REST كاملة لإدارة:

#### Permission Groups

- إنشاء
- تعديل
- حذف
- عرض
- البحث

#### Permissions

- إنشاء
- تعديل
- حذف
- عرض

#### Position Permissions

- ربط صلاحية بمنصب
- حذف الربط
- عرض صلاحيات المنصب

#### User Permissions

- منح صلاحية مباشرة
- حذف الصلاحية
- عرض صلاحيات المستخدم

---

### Seeders

تم إنشاء Seeders افتراضية تشمل:

- MosquePositionSeeder
- PermissionGroupSeeder
- PermissionSeeder
- PositionPermissionSeeder

مع ترتيب التنفيذ باستخدام:

```
@Order
```

---

### Constants

تم إنشاء ثوابت مركزية لاستخدام الأكواد داخل المشروع:

- MosquePositionCodes
- PermissionGroupCodes
- PermissionCodes

---

### Exceptions

تم إضافة استثناءات مخصصة مثل:

- PermissionNotFoundException
- DuplicatePermissionException
- DuplicatePermissionGroupException
- DuplicatePositionPermissionException
- DuplicateUserPermissionException
- UserPermissionNotFoundException
- PositionPermissionNotFoundException

---

### API Infrastructure

تم اعتماد:

- ApiResponse
- PageResponse
- ApiResponseBuilder
- ApiMessage

لتوحيد جميع استجابات واجهات REST.

---

### Integration Tests

تم إنشاء اختبارات تكامل تغطي:

- إنشاء Permission Group.
- إنشاء Permission.
- ربط Permission بمنصب.
- إنشاء User.
- إنشاء Mosque.
- إنشاء Membership.
- التحقق من Authorization الكامل.

---

## Changed

- إعادة هيكلة طبقة Authorization لتصبح مبنية على Resolver Pattern.
- فصل مسؤوليات AuthorizationProvider عن منطق استخراج الصلاحيات.
- تحسين تصميم Permission Resolution باستخدام Composite Pattern.
- تحسين تنظيم Seeders وترتيب تنفيذها.
- استبدال القيم النصية المتكررة (Magic Strings) بثوابت مركزية (Constants).
- ربط خدمات الصلاحيات بنظام Permission Cache لتحديث البيانات تلقائيًا عند الإضافة أو الحذف.

---

## Fixed

- إصلاح مشكلة عدم حفظ حقل `code` في PermissionGroup.
- إصلاح تحديث Permission Cache بعد تعديل صلاحيات المستخدم.
- إصلاح تحديث Permission Cache بعد تعديل صلاحيات المناصب.
- إصلاح مشاكل ربط PositionPermission أثناء الاختبارات.
- إصلاح سيناريو Authorization Integration Test حتى يعمل بنجاح.
- تحسين معالجة الأخطاء داخل طبقة Authorization.

---

# [0.5.0] - 2026-07-11

## Added

### Dynamic Authorization Foundation

- إنشاء Permission Group Module.
- إنشاء Permission Module.
- إنشاء PositionPermission Module.
- إنشاء UserPermission Module.
- بناء الأساس الأول لمحرك Authorization.

---

# [0.4.0] - 2026-07-10

## Added

### Dynamic Permissions

- إنشاء Permission Entity.
- إنشاء PermissionGroup Entity.
- إنشاء PositionPermission Entity.
- إنشاء UserPermission Entity.
- ربط الصلاحيات بالمناصب.

---

# [0.3.0] - 2026-07-09

## Added

### Mosques Module

- Mosque CRUD.
- Mosque Positions.
- Mosque Memberships.
- REST APIs.
- Validation.
- Global Exception Handling.

---

# [0.2.0] - 2026-07-08

## Added

### Users Module

- User Entity.
- Role Entity.
- User Registration.
- Login.
- JWT Authentication.
- Password Encryption.
- Roles Management.

---

# [0.1.0] - 2026-07-07

## Added

### Project Foundation

- إنشاء مستودع GitHub.
- إعداد مشروع Spring Boot.
- إعداد مشروع Angular.
- إعداد PostgreSQL.
- تنظيم هيكل المشروع.
- إعداد Maven.
- إعداد Git.
- إنشاء مجلد الوثائق.
- إنشاء البنية الأساسية للمشروع.

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 7 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# CHANGELOG

جميع التغييرات المهمة في مشروع **محسنون (Mohsinon Platform)** يتم توثيقها في هذا الملف.

يعتمد هذا المشروع على مبدأ **Keep a Changelog** مع استخدام **Semantic Versioning**.

---

# [0.7.0-dev] - 2026-07-12

## 🎯 Milestone 0.7 — Mosque Administration

يمثل هذا الإصدار الانتقال من بناء البنية الأساسية (Infrastructure) إلى بناء أول وحدة أعمال متكاملة في المنصة.

---

## Added

### Mosque Administration

* إضافة دورة حياة متكاملة للعضوية (Membership Lifecycle).
* إضافة `MembershipStatus`.
* إضافة حالات:

  * ACTIVE
  * SUSPENDED
  * TERMINATED
* إضافة منطق تعيين الأعضاء.
* إضافة تغيير الإمام.
* إضافة إنهاء العضوية.
* إضافة استرجاع الإمام الحالي.
* إضافة سجل الأئمة.
* إضافة سجل عضويات المستخدم.

---

### Dashboard Engine

* إضافة Mosque Dashboard.
* إضافة Dashboard Service.
* إضافة Dashboard Controller.
* إضافة Dashboard Registry.
* إضافة Dashboard Provider Interface.
* إضافة Membership Statistics Provider.
* إضافة Position Statistics Provider.
* دعم Plug-in Architecture لإحصائيات المسجد.

---

### Audit Engine

* إضافة Audit Annotation.
* إضافة Audit Aspect.
* إضافة Audit Service.
* إضافة Audit Registry.
* إضافة Audit Provider Interface.
* إضافة Membership Assign Audit Provider.
* إضافة AuditLog Entity.
* إضافة Audit Repository.
* إضافة Audit DTO.
* إضافة AuditAction.
* إضافة AuditEntityType.
* إضافة AuditableResource.

---

### Domain Model

* اعتماد Rich Domain Model داخل MosqueMembership.
* إضافة:

  * activate()
  * suspend()
  * terminate()

---

## Changed

### Membership

* استبدال Active Boolean بـ MembershipStatus.
* تحديث جميع الخدمات للاعتماد على MembershipStatus.
* تحديث جميع الـ Repositories.
* تحديث جميع الـ DTOs.
* تحديث الـ Mapper.
* تحديث اختبارات التكامل.

---

### Dashboard

* تحويل Dashboard إلى Plug-in Architecture.
* فصل الإحصائيات داخل Providers مستقلة.
* إزالة أي اعتماد مباشر على وحدات الأعمال داخل DashboardService.

---

### Audit

* اعتماد Annotation-Based Audit.
* فصل بناء وصف العمليات داخل Audit Providers.
* اعتماد Registry Pattern لإدارة Providers.
* جعل Audit Engine مستقلاً عن وحدات النظام.

---

## Refactored

* إعادة كتابة MosqueMembershipService.
* تبسيط منطق تعيين المناصب.
* تبسيط منطق تغيير الإمام.
* إزالة جميع استدعاءات:

  * setActive(true)
  * setActive(false)
* نقل منطق تغيير حالة العضوية إلى الكيان نفسه.

---

## Improved

* تحسين قابلية التوسع.
* تحسين فصل المسؤوليات (Separation of Concerns).
* تحسين إعادة الاستخدام (Reusability).
* تحسين تصميم الـ Services.
* تحسين تنظيم المشروع.
* توحيد نمط Registry في المحركات المختلفة.

---

## Architecture

تم إنشاء ثلاثة محركات مستقلة داخل المنصة:

* Authorization Engine
* Dashboard Engine
* Audit Engine

لتصبح جميع الوحدات المستقبلية قابلة للإضافة دون تعديل البنية الحالية.

---

## Tests

تم اختبار:

* Membership Assignment
* Change Imam
* Membership Termination
* Membership History
* Dashboard Providers
* Authorization Integration
* Audit Registration

---

# [0.6.0-dev] - 2026-07-11

## Permission Engine

### Added

* Position Permission Management.
* User Permission Management.
* Permission Cache.
* Composite Permission Resolver.
* Permission Resolution Layer.
* Authorization Registry.
* Authorization Providers.
* @RequirePermission Annotation.
* Permission Aspect.
* Integration Tests.

---

# [0.5.0-dev] - 2026-07-10

## Authorization Framework

### Added

* AuthorizationService.
* Spring Security Integration.
* Mosque Authorization Provider.
* Dynamic Permission Resolution.

---

# [0.4.0-dev] - 2026-07-09

## Membership & Positions

### Added

* Mosque Membership Module.
* Mosque Positions Module.
* Membership CRUD.
* Position CRUD.

---

# [0.3.0-dev] - 2026-07-08

## Mosque Module

### Added

* Mosque Entity.
* Mosque Repository.
* Mosque Service.
* Mosque Controller.
* Mosque APIs.

---

# [0.2.0-dev] - 2026-07-08

## Authentication

### Added

* User Registration.
* Login.
* JWT Authentication.
* Password Encryption.
* Spring Security Configuration.

---

# [0.1.0-dev] - 2026-07-07

## Initial Project Setup

### Added

* Spring Boot Project.
* Angular Workspace.
* PostgreSQL Configuration.
* Maven Configuration.
* Project Structure.
* Git Repository.
* Initial Documentation.
* Development Roadmap.
* Vision Document.

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 8 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# CHANGELOG.md

جميع التغييرات المهمة في مشروع **منصة محسنون (Mohsinon Platform)** سيتم توثيقها في هذا الملف.

يعتمد هذا السجل على مبادئ **Keep a Changelog** مع الالتزام بالإصدارات المرحلية (Milestones) الخاصة بالمشروع.

---

# [0.8.0] - 2026-07-13

## 🎉 Milestone 0.8 — Donation Management Foundation

تم إنشاء أول وحدة أعمال (Business Module) تعتمد بالكامل على النواة التي تم تطويرها خلال الأيام السابقة.

---

## Added

### Donation Module

* إنشاء وحدة **Donations**.
* اعتماد Modular Architecture للوحدة.
* إنشاء جميع الحزم القياسية:

  * controller
  * service
  * repository
  * entity
  * dto
  * mapper
  * exception
  * constants
  * model
  * dashboard
  * audit
  * validation

---

### Donation Category

تم إنشاء:

* DonationCategory Entity
* DonationCategoryRepository
* DonationCategoryService
* DonationCategoryController

إضافة DTOs:

* CreateDonationCategoryRequest
* UpdateDonationCategoryRequest
* DonationCategoryResponse

---

### Donation

تم إنشاء:

* Donation Entity
* DonationRepository
* DonationService
* DonationController

إضافة DTOs:

* CreateDonationRequest
* DonationResponse

---

### Donation Lifecycle

تم اعتماد دورة حياة واضحة للتبرع.

الحالات:

* PENDING
* RECEIVED
* ALLOCATED
* DELIVERED
* CANCELLED

كما تمت إضافة العمليات:

* receive()
* allocate()
* deliver()
* cancel()

---

### Enums

إضافة:

* DonationType
* DonationStatus

---

### Mapper

تم إنشاء:

* DonationMapper

لدعم:

* Entity ↔ DTO Mapping
* Category Mapping
* Update Mapping

---

### Exception Handling

إضافة:

* DonationNotFoundException
* DonationCategoryNotFoundException
* DonationCategoryAlreadyExistsException

---

### Authorization Integration

تم دمج وحدة التبرعات مع:

* Authorization Engine
* Permission Engine

وإضافة مجموعة صلاحيات خاصة بالتبرعات.

---

### Dashboard Integration

إضافة:

* DonationStatisticsProvider

كما تم توسيع:

* MosqueDashboardResponse

ليدعم مؤشرات التبرعات.

---

### Audit Integration

تم تجهيز وحدة التبرعات للعمل مع:

* Audit Engine

وإضافة أول Audit Provider خاص بالتبرعات.

---

## Changed

### Architecture

إثبات إمكانية إضافة وحدة أعمال جديدة دون تعديل:

* Authorization Engine
* Dashboard Engine
* Audit Engine

---

### Domain Model

تحويل Donation إلى كيان غني بالسلوك (Rich Domain Model) يعتمد على Domain Methods لإدارة دورة الحياة بدلاً من تحديث الحالة مباشرة.

---

### Dashboard

اعتماد استمرار استخدام **Provider Pattern** لإضافة مؤشرات جديدة دون تعديل محرك لوحة المعلومات.

---

## Deferred

تم تأجيل:

* Integration Tests
* تحسين استعلامات الإحصائيات باستخدام `countBy...`
* إعادة تصميم Audit Providers لتقليل تكرار الملفات

وسيتم تنفيذها ضمن Sprint إعادة الهيكلة القادم.

---

# [0.7.0] - 2026-07-12

## Milestone 0.7 — Dashboard & Audit Engine

### Added

* Dashboard Engine
* Provider Registry
* Membership Statistics Provider
* Position Statistics Provider
* Audit Engine
* Audit Providers
* Audit Registry
* Dynamic Dashboard Extension

---

# [0.6.0] - 2026-07-11

## Milestone 0.6 — Authorization Engine

### Added

* Authorization Service
* Permission Checking
* Position Permissions
* User Permissions
* RequirePermission Annotation
* Authorization Aspect

---

# [0.5.0] - 2026-07-10

## Milestone 0.5 — Permission Engine

### Added

* Permission Groups
* Permissions
* Position Permission Management
* Dynamic Permission Model

---

# [0.4.0] - 2026-07-09

## Milestone 0.4 — Membership Lifecycle

### Added

* Mosque Membership Management
* Membership Lifecycle
* Activation
* Suspension
* Termination

---

# [0.3.0] - 2026-07-08

## Milestone 0.3 — Mosques Module

### Added

* Mosque Entity
* Mosque Repository
* Mosque Service
* Mosque Controller
* CRUD APIs

---

# [0.2.0] - 2026-07-08

## Milestone 0.2 — Users & Authentication

### Added

* User Entity
* Role Entity
* Registration
* Login
* JWT Authentication
* BCrypt Password Encryption

---

# [0.1.0] - 2026-07-07

## Milestone 0.1 — Project Foundation

### Added

* إنشاء مستودع GitHub
* إعداد مشروع Spring Boot
* إعداد Angular
* إعداد PostgreSQL
* اعتماد Modular Architecture
* إنشاء الهيكل الأساسي للمشروع
* إعداد بيئة التطوير

---

# Summary

| Version | Milestone                      | Status |
| ------- | ------------------------------ | ------ |
| 0.1.0   | Project Foundation             | ✅      |
| 0.2.0   | Users & Authentication         | ✅      |
| 0.3.0   | Mosques Module                 | ✅      |
| 0.4.0   | Membership Lifecycle           | ✅      |
| 0.5.0   | Permission Engine              | ✅      |
| 0.6.0   | Authorization Engine           | ✅      |
| 0.7.0   | Dashboard & Audit Engine       | ✅      |
| 0.8.0   | Donation Management Foundation | ✅      |

---

**Current Version:** **0.8.0**
**Current Milestone:** **Donation Management Foundation**
**Project Status:** 🟢 Active Development

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 9 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 10 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 11 ✅
#### ##### #### ##### #### ##### #### ##### #### #####