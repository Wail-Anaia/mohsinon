# Mohsinon Platform Architecture

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 1 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

> الإصدار: 0.1.0
> آخر تحديث: 2026-07-07

---

# مقدمة

تصف هذه الوثيقة المعمارية (Architecture) المعتمدة في مشروع **Mohsinon**.

الهدف هو بناء منصة قابلة للتوسع والصيانة والاختبار، مع الالتزام بأفضل الممارسات الحديثة في تطوير البرمجيات.

---

# الرؤية المعمارية

تم تصميم المشروع ليكون:

* Modular
* Scalable
* Maintainable
* Secure
* Testable
* Production Ready

---

# التقنيات المستخدمة

## Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Maven
* PostgreSQL
* Bean Validation
* Lombok

---

## Frontend

* Angular (Standalone)
* Angular Material
* Angular Router
* SCSS

---

## Database

* PostgreSQL

---

## Version Control

* Git
* GitHub

---

# الهيكل العام للمشروع

```text
mohsinon/
│
├── backend/
├── frontend/
├── docs/
│
├── README.md
├── .gitignore
└── LICENSE
```

---

# معمارية Backend

سيتم اتباع **Layered Architecture**.

كل وحدة (Module) ستكون مستقلة قدر الإمكان.

مثال:

```text
users/
│
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
│   ├── request/
│   └── response/
├── mapper/
└── validation/
```

سيتم تطبيق نفس الهيكل على بقية الوحدات.

---

# الوحدات الأساسية

سيتم تقسيم النظام إلى Modules مستقلة.

```text
auth
users
roles
mosques
initiatives
donations
volunteers
notifications
reports
administration
```

كل Module يحتوي على مسؤولية واحدة فقط.

---

# الطبقات البرمجية

## Controller Layer

المسؤول عن استقبال طلبات HTTP وإرجاع Responses.

لا يحتوي على Business Logic.

---

## Service Layer

المسؤول عن تنفيذ منطق العمل (Business Logic).

---

## Repository Layer

المسؤول عن التعامل مع قاعدة البيانات باستخدام Spring Data JPA.

---

## Entity Layer

تمثل جداول قاعدة البيانات.

---

## DTO Layer

تستخدم لنقل البيانات بين العميل والخادم.

لن يتم إرسال الـ Entities مباشرة إلى الواجهة الأمامية.

---

# تصميم قاعدة البيانات

سيتم استخدام:

* UUID للمفاتيح الأساسية حيث يناسب ذلك.
* علاقات JPA المناسبة (OneToMany، ManyToOne، ManyToMany، OneToOne).
* قيود (Constraints) للحفاظ على سلامة البيانات.
* فهارس (Indexes) عند الحاجة لتحسين الأداء.

---

# Security Architecture

سيتم الاعتماد على:

* Spring Security
* JWT Authentication
* Refresh Tokens
* Role-Based Authorization

الأدوار المخطط لها:

* ADMIN
* IMAM
* MOSQUE_COMMITTEE
* VOLUNTEER
* DONOR
* USER

---

# API Design

سيتم تصميم جميع الواجهات البرمجية وفق REST.

مثال:

```text
POST   /api/v1/auth/register
POST   /api/v1/auth/login

GET    /api/v1/users
GET    /api/v1/users/{id}

POST   /api/v1/mosques
PUT    /api/v1/mosques/{id}
DELETE /api/v1/mosques/{id}
```

---

# معالجة الأخطاء

سيتم إنشاء معالج أخطاء موحد باستخدام Global Exception Handler.

ستكون جميع رسائل الخطأ بصيغة موحدة.

مثال:

```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "...",
  "path": "/api/v1/..."
}
```

---

# Validation

سيتم استخدام Bean Validation.

مثل:

* @NotBlank
* @Email
* @Size
* @Pattern

وسيتم التحقق من صحة البيانات قبل تنفيذ أي Business Logic.

---

# Logging

سيتم استخدام نظام Logging الخاص بـ Spring Boot.

وسيتم تسجيل:

* الأخطاء.
* التحذيرات.
* الأحداث المهمة.

مع تجنب تسجيل البيانات الحساسة.

---

# Testing

سيتم اعتماد نوعين من الاختبارات:

## Unit Tests

لاختبار الخدمات والمنطق البرمجي.

## Integration Tests

لاختبار التكامل بين الطبقات وقاعدة البيانات وواجهات REST.

---

# Frontend Architecture

سيتم تقسيم Angular إلى وحدات واضحة.

```text
core/
shared/
features/
layout/
guards/
services/
models/
```

وسيكون لكل Feature مكوناته وخدماته الخاصة.

---

# التوثيق

سيتم توثيق المشروع باستخدام:

```text
docs/
│
├── PROJECT_STATUS.md
├── CHANGELOG.md
├── ARCHITECTURE.md
├── ROADMAP.md
├── VISION.md
├── REQUIREMENTS.md
└── daily/
```

وسيتم تحديث هذه الملفات بشكل دوري.

---

# المبادئ الهندسية

يلتزم المشروع بالمبادئ التالية:

* Clean Code
* SOLID Principles
* Separation of Concerns
* DRY (Don't Repeat Yourself)
* KISS (Keep It Simple, Stupid)
* YAGNI (You Aren't Gonna Need It)

وسيتم تطبيقها بصورة عملية دون تعقيد غير ضروري.

---

# معايير جودة الكود

* أسماء واضحة للملفات والكلاسات.
* مسؤولية واحدة لكل Class.
* تجنب تكرار الكود.
* استخدام DTOs بدل كشف Entities.
* توثيق القرارات الهندسية المهمة.
* الالتزام بأسلوب موحد في كتابة الكود.

---

# التطوير المستقبلي

تم تصميم المعمارية بحيث تسمح بإضافة وحدات جديدة دون إعادة هيكلة المشروع.

من أمثلة الوحدات المستقبلية:

* إدارة الجمعيات.
* إدارة المشاريع الخيرية.
* الحملات.
* الذكاء الاصطناعي.
* الإشعارات.
* لوحة التحكم.
* التقارير والإحصائيات.
* تطبيق الهاتف المحمول.

---

# خاتمة

تهدف هذه المعمارية إلى بناء منصة مستقرة وقابلة للتوسع على المدى الطويل.

أي قرار هندسي يؤثر على بنية النظام يجب توثيقه وتحديث هذا الملف، حتى يبقى المرجع الرسمي لمعمارية منصة **Mohsinon**.

#### ##### #### ##### #### ##### #### ##### #### #####
### Day 2 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# ARCHITECTURE.md

آخر تحديث: 2026-07-08

---

# معمارية مشروع منصة محسنون

## مقدمة

تم تصميم منصة **محسنون** منذ البداية وفق معايير المشاريع الإنتاجية (Production Grade)، بحيث تكون قابلة للتوسع لسنوات طويلة دون الحاجة إلى إعادة كتابة النظام.

يعتمد المشروع على مبدأ **Modular Monolith** في البداية، مع إمكانية التحول مستقبلاً إلى **Microservices** عند الحاجة.

---

# المبادئ الأساسية

يعتمد المشروع على المبادئ التالية:

- Clean Architecture
- Layered Architecture
- SOLID Principles
- Domain Driven Design (بشكل تدريجي)
- RESTful APIs
- Stateless Authentication
- JWT Authentication
- DTO Pattern
- Dependency Injection
- Separation of Concerns
- High Cohesion
- Low Coupling

---

# الهيكل العام للمشروع

```
mohsinon/
│
├── backend/
│
├── frontend/
│
├── docs/
│
├── README.md
│
└── .gitignore
```

---

# Backend Architecture

```
backend/
│
├── src/main/java/com/mohsinon
│
├── auth/
├── users/
├── role/
├── mosques/
├── volunteers/
├── donations/
├── campaigns/
├── charities/
├── notifications/
├── dashboard/
├── reports/
│
├── common/
├── config/
├── security/
├── exception/
├── util/
│
└── BackendApplication.java
```

---

# هيكل كل Module

كل وحدة داخل النظام مستقلة قدر الإمكان.

مثال:

```
users/
│
├── controller
│
├── service
│
├── repository
│
├── entity
│
├── dto
│
│   ├── request
│   └── response
│
├── mapper
│
└── validator
```

وبذلك يصبح كل Module مسؤولاً عن نفسه فقط.

---

# الطبقات (Layers)

## 1. Controller Layer

المسؤوليات:

- استقبال الطلبات
- التحقق من البيانات
- استدعاء Service
- إعادة Response

لا يحتوي على أي Business Logic.

---

## 2. Service Layer

القلب الحقيقي للنظام.

المسؤوليات:

- Business Logic
- Validation
- Rules
- التعامل مع أكثر من Repository
- استدعاء الخدمات الأخرى

---

## 3. Repository Layer

المسؤوليات:

- الوصول إلى قاعدة البيانات
- تنفيذ الاستعلامات
- حفظ البيانات
- حذف البيانات
- تحديث البيانات

يعتمد على Spring Data JPA.

---

## 4. Entity Layer

يمثل جداول قاعدة البيانات.

يحتوي فقط على:

- الحقول
- العلاقات
- القيود
- Lifecycle Events

ولا يحتوي على منطق الأعمال.

---

## 5. DTO Layer

يستخدم لنقل البيانات بين العميل والخادم.

ينقسم إلى:

```
Request DTO
```

و

```
Response DTO
```

ولا يتم إرسال الـ Entity مباشرة إلى الواجهة الأمامية.

---

## 6. Mapper Layer

مسؤول عن التحويل بين:

```
Entity
⇅
DTO
```

مما يحافظ على استقلالية الطبقات.

---

## 7. Validator Layer

يتحقق من صحة البيانات وفق قواعد العمل الخاصة بكل وحدة، إضافةً إلى Validation القياسي.

---

# Security Architecture

يعتمد النظام على:

```
Spring Security
```

مع:

```
JWT Authentication
```

بدون استخدام Sessions.

---

## آلية العمل

```
Client

↓

Login

↓

JWT Token

↓

Authorization Header

↓

JWT Filter

↓

Security Context

↓

Controller
```

---

# Database Architecture

يعتمد المشروع على:

```
PostgreSQL
```

ويستخدم:

```
Hibernate ORM
```

و

```
Spring Data JPA
```

---

# تصميم قاعدة البيانات

كل Module يمتلك الجداول الخاصة به.

مثال:

```
Users

Roles

Mosques

Volunteers

Donations

Projects

Campaigns

Notifications

Audit Logs
```

العلاقات بين الجداول تكون واضحة ومحددة باستخدام مفاتيح خارجية (Foreign Keys).

---

# Frontend Architecture

يعتمد المشروع على:

```
Angular
```

بنظام:

```
Standalone Components
```

---

هيكل الواجهة:

```
src/

app/

core/

shared/

features/

layouts/

pages/

guards/

services/

interceptors/
```

---

# Core

يحتوي على:

- Authentication
- Guards
- Interceptors
- Config
- Services

---

# Shared

يحتوي على:

- Components
- Directives
- Pipes
- Models
- Utilities

---

# Features

كل Module يمتلك مجلده الخاص.

مثال:

```
users

mosques

volunteers

donations

campaigns
```

---

# API Architecture

جميع الخدمات تعمل عبر REST APIs.

مثال:

```
POST

/api/auth/login
```

```
POST

/api/auth/register
```

```
GET

/api/users
```

```
POST

/api/mosques
```

```
PUT

/api/mosques/{id}
```

```
DELETE

/api/mosques/{id}
```

---

# Authentication Flow

```
Register

↓

Database

↓

Login

↓

JWT

↓

Frontend

↓

Authorization Header

↓

Backend

↓

Response
```

---

# Exception Handling

يوجد معالج أخطاء موحد.

```
GlobalExceptionHandler
```

ويعيد جميع الأخطاء بصيغة JSON موحدة.

---

# Logging

سيستخدم المشروع:

- SLF4J
- Logback

مع إمكانية إضافة مراقبة متقدمة لاحقًا.

---

# الملفات المشتركة

سيحتوي المشروع مستقبلاً على مجلد:

```
common/
```

ويضم:

- Constants
- Enums
- Utilities
- BaseEntity
- Common DTOs

---

# الوحدات المخطط لها

## المرحلة الأولى

- Users
- Authentication
- Roles

---

## المرحلة الثانية

- Mosques
- Imams
- Mosque Committees

---

## المرحلة الثالثة

- Volunteers
- Charities
- Donations

---

## المرحلة الرابعة

- Campaigns
- Projects
- Events

---

## المرحلة الخامسة

- Notifications
- Reports
- Dashboard
- Statistics

---

## المرحلة السادسة

- Mobile API
- AI Assistant
- Public API
- Integrations

---

# مبادئ التطوير

يلتزم المشروع بما يلي:

- كتابة كود نظيف وقابل للقراءة.
- فصل المسؤوليات بين الطبقات.
- استخدام DTO بدلاً من Entity في الواجهات.
- الاعتماد على Dependency Injection.
- كتابة وحدات مستقلة قابلة لإعادة الاستخدام.
- تصميم يسهل الاختبار والصيانة.
- تجنب التكرار (DRY).
- الالتزام بمبدأ المسؤولية الواحدة (SRP).

---

# الهدف النهائي

بناء منصة خيرية متكاملة ذات بنية قوية وقابلة للتوسع، يمكنها مستقبلاً خدمة ملايين المستخدمين، مع المحافظة على جودة الكود وسهولة التطوير والصيانة.

#### ##### #### ##### #### ##### #### ##### #### #####
### Day 3 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# ARCHITECTURE
## منصة محسنون (Mohsinon Platform)

**آخر تحديث:** 2026-07-09

---

# مقدمة

تم تصميم منصة **محسنون** وفق معمارية حديثة تعتمد على **Modular Monolith Architecture** في المرحلة الأولى من المشروع، مع مراعاة إمكانية التحول مستقبلاً إلى **Microservices Architecture** دون الحاجة إلى إعادة كتابة النظام.

تعتمد المنصة على مبدأ **Separation of Concerns** وفصل المسؤوليات بين الطبقات والوحدات، مما يجعل المشروع سهل الصيانة، وقابلاً للتوسع، وسهل الاختبار.

---

# المبادئ المعمارية

يعتمد المشروع على المبادئ التالية:

- Modular Architecture
- Layered Architecture
- Clean Code
- SOLID Principles
- Domain-Driven Design (DDD) - Lite
- RESTful API Design
- Dependency Injection
- Convention over Configuration

---

# الهيكل العام للمشروع

```text
mohsinon/
│
├── backend/
│
├── frontend/
│
├── docs/
│
├── .gitignore
│
├── README.md
│
└── LICENSE
```

---

# Backend Architecture

```text
backend
│
└── src/main/java/com/mohsinon
    │
    ├── BackendApplication.java
    │
    ├── common/
    │
    ├── config/
    │
    ├── exception/
    │
    ├── security/
    │
    └── modules/
```

---

# مجلد modules

يحتوي على جميع وحدات النظام (Business Modules).

كل وحدة مستقلة عن الأخرى وتحتوي على جميع الطبقات الخاصة بها.

```text
modules/
│
├── user/
├── mosque/
├── initiative/
├── donation/
├── volunteer/
├── committee/
├── event/
├── notification/
├── file/
└── ...
```

---

# هيكل الوحدة الواحدة

كل وحدة تتبع نفس النمط.

مثال:

```text
modules/mosque
│
├── controller
├── dto
│   ├── request
│   └── response
│
├── entity
├── mapper
├── repository
└── service
```

هذا يجعل جميع الوحدات متجانسة وسهلة التطوير والصيانة.

---

# الطبقات (Layers)

## Controller Layer

مسؤولة عن:

- استقبال طلبات HTTP.
- التحقق من صحة البيانات.
- استدعاء الخدمات.
- إرجاع Response مناسب.

لا تحتوي على منطق الأعمال (Business Logic).

---

## Service Layer

تمثل قلب النظام.

مسؤولة عن:

- تنفيذ Business Logic.
- استدعاء Repository.
- تطبيق قواعد العمل.
- إدارة العمليات.

---

## Repository Layer

تعتمد على Spring Data JPA.

مسؤولة عن:

- قراءة البيانات.
- حفظ البيانات.
- تنفيذ الاستعلامات.

ولا تحتوي على أي Business Logic.

---

## Entity Layer

تمثل جداول قاعدة البيانات.

كل Entity يعبر عن كيان حقيقي داخل النظام.

مثل:

- User
- Role
- Mosque
- Donation
- Initiative

---

## DTO Layer

تعتمد المنصة على فصل البيانات الداخلة والخارجة عن Entity.

### Request DTO

يمثل البيانات القادمة من العميل.

مثال:

```text
CreateMosqueRequest
```

---

### Response DTO

يمثل البيانات المرسلة إلى العميل.

مثال:

```text
MosqueResponse
```

---

# Mapper Layer

مسؤولة عن تحويل البيانات بين:

```text
DTO
⇅
Entity
```

ويتم حالياً تنفيذها يدوياً.

ويمكن مستقبلاً استخدام:

- MapStruct

لتوليد الكود تلقائياً.

---

# Common Package

يحتوي على المكونات المشتركة بين جميع الوحدات.

مستقبلاً سيضم:

```text
common
│
├── constants
├── enums
├── utils
├── validators
├── events
└── base
```

---

# Exception Package

يحتوي على النظام المركزي لمعالجة الأخطاء.

```text
exception
│
├── ApiError
├── GlobalExceptionHandler
├── ResourceNotFoundException
├── ResourceAlreadyExistsException
├── AuthenticationException
└── ...
```

جميع وحدات النظام تستخدم نفس آلية معالجة الأخطاء.

---

# Config Package

يحتوي على إعدادات المشروع.

مثل:

- CORS
- Jackson
- Swagger
- Bean Configuration
- Cache
- OpenAPI

---

# Security Package

مسؤول عن:

- Spring Security
- JWT
- Authentication
- Authorization
- Security Filters

---

# قاعدة البيانات

يعتمد المشروع على PostgreSQL.

جميع الكيانات تستخدم:

```text
UUID
```

كمفتاح أساسي.

مثال:

```java
@Id
@GeneratedValue
private UUID id;
```

---

# أسلوب تصميم REST APIs

تعتمد جميع الواجهات على RESTful Design.

مثال:

```http
GET    /api/mosques

GET    /api/mosques/{id}

POST   /api/mosques

PUT    /api/mosques/{id}

DELETE /api/mosques/{id}
```

---

# تدفق الطلب داخل النظام

```text
HTTP Request
        │
        ▼
Controller
        │
        ▼
Service
        │
        ▼
Repository
        │
        ▼
Database
        │
        ▲
Repository
        │
        ▲
Service
        │
        ▲
Controller
        │
        ▲
HTTP Response
```

---

# معالجة البيانات

البيانات تمر بالمراحل التالية:

```text
JSON Request
      │
      ▼
Request DTO
      │
      ▼
Mapper
      │
      ▼
Entity
      │
      ▼
Repository
```

وعند الإرجاع:

```text
Entity
      │
      ▼
Mapper
      │
      ▼
Response DTO
      │
      ▼
JSON Response
```

---

# التحقق من صحة البيانات

يعتمد المشروع على:

Bean Validation

باستخدام:

```java
@NotBlank

@NotNull

@Email

@Size

@Valid
```

---

# معالجة الأخطاء

يستخدم المشروع:

```text
GlobalExceptionHandler
```

لتوحيد جميع Responses الخاصة بالأخطاء.

بدلاً من ظهور رسائل Spring الافتراضية.

---

# الوحدات الحالية

## User Module

يتضمن:

- المستخدمين
- الأدوار
- المصادقة
- JWT

---

## Mosque Module

يتضمن:

- بيانات المسجد
- إدارة المساجد
- REST APIs
- DTO
- Mapper

---

# الوحدات القادمة

سيتم تطوير:

```text
Imam Module

Mosque Committee Module

Volunteer Module

Donation Module

Need Module

Initiative Module

Campaign Module

Notification Module

Event Module

File Module
```

---

# Frontend Architecture

يعتمد المشروع على Angular Standalone.

الهيكل المخطط:

```text
frontend
│
├── core
├── shared
├── layouts
├── features
│   ├── auth
│   ├── home
│   ├── mosque
│   ├── profile
│   └── ...
├── assets
└── environments
```

سيتم تطوير الواجهة الأمامية بعد تثبيت النواة الأساسية للـ Backend.

---

# قابلية التوسع

تم تصميم المعمارية بحيث تدعم:

- إضافة وحدات جديدة دون التأثير على الوحدات الحالية.
- تحويل الوحدات إلى Microservices مستقبلاً.
- التوسع الأفقي مع زيادة عدد المستخدمين.
- سهولة الاختبار الآلي (Unit & Integration Testing).
- سهولة الصيانة وإعادة الاستخدام.

---

# القرارات المعمارية الرئيسية

- اعتماد Modular Monolith Architecture.
- استخدام Layered Architecture داخل كل وحدة.
- تنظيم المشروع حسب المجالات (Modules).
- فصل الـ DTO عن الـ Entity.
- استخدام Mapper للتحويل بين الطبقات.
- اعتماد UUID كمفتاح أساسي.
- توحيد نظام معالجة الاستثناءات.
- استخدام Bean Validation.
- تصميم REST APIs وفق أفضل الممارسات.
- تجهيز المشروع للانتقال مستقبلاً إلى Microservices إذا دعت الحاجة.

---

# الخلاصة

تمثل هذه المعمارية الأساس التقني لمنصة **محسنون**، حيث توازن بين البساطة في المرحلة الحالية والقابلية للتوسع مستقبلاً. ويضمن هذا التصميم إمكانية إضافة وحدات جديدة، وزيادة حجم النظام، وعدد المستخدمين، دون الحاجة إلى إعادة هيكلة جذرية، مما يجعل المشروع مستدامًا وقادرًا على التطور مع نمو المنصة.

#### ##### #### ##### #### ##### #### ##### #### #####
### Day 4 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# ARCHITECTURE

**المشروع:** منصة محسنون (Mohsinon Platform)

**آخر تحديث:** 2026-07-09 (نهاية DAY 04)

---

# الرؤية المعمارية

تم تصميم منصة **محسنون** وفق معمارية **Modular Monolith** بحيث تكون كل وحدة مستقلة منطقياً، مع إمكانية تحويلها مستقبلاً إلى Microservice دون الحاجة إلى إعادة كتابة الكود.

يعتمد المشروع على مبادئ:

- Clean Architecture
- Domain Driven Design (DDD)
- SOLID Principles
- Separation of Concerns
- Feature-based Modules

---

# الطبقات الرئيسية

```
Frontend (Angular)

        │
        ▼

REST Controllers

        │
        ▼

Services

        │
        ▼

Repositories

        │
        ▼

Database (PostgreSQL)
```

---

# الهيكل العام للمشروع

```
mohsinon/

│
├── backend/
│
├── frontend/
│
├── docs/
│
└── README.md
```

---

# هيكل Backend

```
backend/

src/main/java/com/mohsinon/

│
├── config
│
├── common
│
├── security
│
├── modules
│
└── exception
```

---

# الوحدات (Modules)

كل وحدة تمثل نطاقاً مستقلاً داخل المنصة.

```
modules/

users/

mosques/

authorization/

associations/

projects/

volunteers/

donations/

initiatives/

marketplace/

notifications/

files/

ai/
```

كل Module يحتوي على بنيته الخاصة.

مثال:

```
modules/mosques/

entity/

repository/

service/

controller/

dto/

mapper/

validator/

event/
```

---

# وحدة المستخدمين

```
users

│
├── User
│
├── Role
│
├── Authentication
│
└── JWT
```

المسؤوليات:

- إنشاء الحسابات
- تسجيل الدخول
- JWT
- كلمات المرور
- الأدوار العامة

---

# وحدة المساجد

```
Mosque

│
├── Memberships
│
├── Positions
│
└── Authorization
```

---

العلاقات

```
Mosque

│

├── MosqueMembership

│       │

│       ├── User

│       └── MosquePosition
```

---

# وحدة الصلاحيات

```
Permission Groups

        │

        ▼

Permissions

        │

        ▼

Position Permissions

        │

        ▼

Mosque Positions
```

بالتوازي

```
Users

        │

        ▼

User Permissions
```

---

# محرك الصلاحيات

أي عملية تمر بالتسلسل التالي:

```
Current User

        │

        ▼

User Permissions

        │

        ▼

Membership

        │

        ▼

Position

        │

        ▼

Position Permissions

        │

        ▼

Permission Granted ?

        │

   نعم ─────► Execute

   لا ──────► Access Denied
```

---

# قاعدة البيانات

الجداول الحالية

```
users

roles

users_roles

mosques

mosque_positions

mosque_memberships

permission_groups

permissions

position_permissions

user_permissions
```

---

# العلاقات

```
User

│

├── Roles

├── Memberships

└── UserPermissions
```

---

```
Mosque

│

├── Memberships

└── Positions
```

---

```
MosquePosition

│

├── PositionPermissions

└── Memberships
```

---

```
PermissionGroup

│

└── Permissions
```

---

```
Permission

│

├── PositionPermissions

└── UserPermissions
```

---

# هيكل الـ DTO

لكل وحدة يوجد:

```
dto/

request/

response/
```

مثال

```
AssignMembershipRequest

MosqueResponse

MosqueMembershipResponse
```

---

# Mapper Layer

يعتمد المشروع على طبقة مستقلة لتحويل الكيانات إلى DTO.

```
Entity

↓

Mapper

↓

Response DTO
```

مثال

```
MosqueMembershipMapper

UserMapper

MosqueMapper
```

---

# Service Layer

الخدمات مسؤولة عن:

- Business Logic
- Validation
- Transactions
- Authorization

ولا تحتوي على أي كود خاص بالـ HTTP.

---

# Repository Layer

تعتمد بالكامل على Spring Data JPA.

كل Repository مسؤول عن Entity واحدة.

---

# Security Layer

```
JWT

↓

JwtAuthenticationFilter

↓

Spring Security

↓

AuthorizationService
```

حالياً يعتمد المشروع على JWT Stateless Authentication.

---

# إدارة الصلاحيات

تم اعتماد تصميم ديناميكي بالكامل.

```
PermissionGroup

↓

Permission

↓

PositionPermission

↓

MosquePosition

↓

MosqueMembership

↓

User
```

ويضاف فوق ذلك

```
UserPermission
```

لإعطاء صلاحيات استثنائية لأي مستخدم.

---

# Seeders

تستخدم لتهيئة النظام تلقائياً عند التشغيل.

حالياً يوجد:

```
MosquePositionSeeder

PermissionGroupSeeder

PermissionSeeder

PositionPermissionSeeder
```

وسيضاف مستقبلاً:

```
RoleSeeder

CountrySeeder

LanguageSeeder

CurrencySeeder
```

---

# المبادئ المعمارية

## عدم تكرار الكود

تم اعتماد مبدأ:

```
Don't Repeat Yourself (DRY)
```

---

## فصل المسؤوليات

كل طبقة تؤدي وظيفة واحدة فقط.

---

## قابلية التوسع

يمكن إضافة أي Module جديد دون التأثير على الوحدات الأخرى.

---

## إعادة الاستخدام

محرك الصلاحيات نفسه سيُستخدم في:

- المساجد
- الجمعيات
- المشاريع
- المبادرات
- التبرعات
- المتطوعين
- سوق الإحسان

---

# التقنيات المستخدمة

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

---

## Database

- PostgreSQL

---

## Frontend

- Angular
- Angular Material
- TypeScript
- SCSS

---

## Authentication

- JWT

---

## Build

- Maven

---

## Version Control

- Git
- GitHub

---

# الحالة الحالية

```
Core Backend
██████████████████████░░░░░░░░░░░░░░░

Users
████████████████████████████████████

Authentication
████████████████████████████████████

Mosques
███████████████████████████████████░

Authorization
█████████████████████████████████░░░

Frontend
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

Associations
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

Projects
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
```

---

# الخطوة التالية

في **DAY 05** سيتم تطوير **Authorization Framework** باستخدام:

- Spring AOP
- Custom Annotations
- @RequirePermission
- Permission Aspect

لجعل تطبيق الصلاحيات تلقائياً على الخدمات ونقاط النهاية (APIs)، مع الحفاظ على نفس محرك الصلاحيات الحالي وإعادة استخدامه في جميع وحدات المنصة.
#### ##### #### ##### #### ##### #### ##### #### #####
### Day 5 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# ARCHITECTURE.md

# معمارية منصة محسنون (Mohsinon Platform)

**آخر تحديث:** 2026-07-10  
**الإصدار:** 0.5.0

---

# نظرة عامة

تعتمد منصة **محسنون** على معمارية Modular Monolith تعتمد على مبدأ فصل الوحدات (Modules)، بحيث تكون كل وحدة مستقلة قدر الإمكان، مع إمكانية فصلها مستقبلاً إلى Microservices دون إعادة كتابة المنطق الأساسي.

يعتمد المشروع على:

- Java 21
- Spring Boot 3.5.x
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT Authentication
- Dynamic Authorization Engine
- Angular (Frontend)

---

# البنية العامة

```
Backend
│
├── common
│
├── security
│
├── modules
│      │
│      ├── users
│      ├── auth
│      ├── mosques
│      ├── authorization
│      ├── associations (future)
│      ├── projects (future)
│      ├── donations (future)
│      ├── volunteers (future)
│      ├── marketplace (future)
│      └── ...
│
└── config
```

---

# طبقات النظام

كل Module يحتوي تقريباً على نفس الطبقات.

```
module
│
├── controller
├── service
├── repository
├── entity
├── dto
├── mapper
├── authorization
├── constants
└── exception
```

---

# Authentication Layer

تعتمد عملية تسجيل الدخول على:

```
JWT

↓

Security Filter

↓

CurrentUserService

↓

User
```

لا يتم استعمال HttpSession نهائياً.

كل Request يحتوي على JWT.

---

# Authorization Layer

من أهم أجزاء المشروع.

تم تصميم النظام ليكون Dynamic بالكامل.

```
Controller

↓

@RequirePermission

↓

PermissionAspect

↓

AuthorizationService

↓

AuthorizationRegistry

↓

AuthorizationProvider

↓

Module Authorization Provider

↓

Database
```

---

# Authorization Providers

كل Module يمتلك AuthorizationProvider خاصاً به.

مثال:

```
MosqueAuthorizationProvider

AssociationAuthorizationProvider

ProjectAuthorizationProvider

DonationAuthorizationProvider

VolunteerAuthorizationProvider
```

وبالتالي لا يعرف أي Module شيئاً عن الآخر.

---

# AuthorizationRegistry

يقوم بتسجيل جميع Providers الموجودة داخل النظام.

```
AuthorizationRegistry

↓

mosque

↓

MosqueAuthorizationProvider


association

↓

AssociationAuthorizationProvider


project

↓

ProjectAuthorizationProvider
```

---

# PermissionAspect

تم نقل التحقق من الصلاحيات إلى AOP.

أي Controller يستطيع فقط كتابة:

```
@RequirePermission(
    groupCode="mosque",
    permission="mosque.view"
)
```

ولا يحتاج إلى كتابة أي سطر Authorization داخل الـ Controller.

---

# Resource Identification

بدلاً من تمرير Entity كاملة يتم تمرير ResourceId.

```
@PathVariable UUID mosqueId

↓

@ResourceId

↓

PermissionAspect

↓

AuthorizationProvider
```

وبذلك لا يعتمد الـ Aspect على أي Module.

---

# Permission Groups

أصبح النظام يعتمد على جدول:

```
permission_groups
```

ويحتوي على جميع الوحدات.

مثال:

```
mosque

association

project

donation

marketplace

volunteer

education

campaign

...
```

وعند إضافة Module جديد لا يتم تعديل الكود.

يكفي إضافة Provider جديد.

---

# قاعدة البيانات

الوحدات الحالية:

```
users

roles

mosques

mosque_positions

mosque_memberships

permissions

permission_groups

position_permissions

user_permissions
```

---

# العلاقة بين المستخدم والمسجد

```
User

↓

MosqueMembership

↓

MosquePosition

↓

PositionPermissions

↓

Permissions
```

أي أن الصلاحيات تأتي من:

- المنصب
- أو الصلاحيات المباشرة للمستخدم

---

# Dynamic Permission Resolution

أولوية التحقق:

```
User Permission

↓

إذا موجودة

↓

Success


وإلا

↓

Mosque Membership

↓

Position

↓

Position Permissions

↓

Permission Exists ?

↓

Success

↓

Otherwise

↓

AccessDeniedException
```

---

# إنشاء مسجد جديد

أصبح النظام ينشئ أول عضوية تلقائياً.

```
Create Mosque

↓

Save Mosque

↓

Current User

↓

Committee President

↓

MosqueMembership
```

وبذلك يصبح منشئ المسجد أول رئيس لجنة.

---

# Current User

يعتمد المشروع على:

```
CurrentUserService
```

بدلاً من استخدام SecurityContext مباشرة داخل الخدمات.

```
Service

↓

CurrentUserService

↓

Authenticated User
```

---

# Security

يتكون النظام حالياً من:

```
JWT

↓

Authentication Filter

↓

CurrentUserService

↓

Authorization Aspect

↓

Authorization Providers
```

---

# تصميم قابل للتوسع

عند إضافة أي Module جديد ستكون الخطوات:

```
إنشاء الجداول

↓

إنشاء Provider

↓

إضافة Permission Group

↓

استعمال @RequirePermission

↓

ينتهي كل شيء
```

لن يتم تعديل:

- AuthorizationService

ولا

- PermissionAspect

ولا

- Security Configuration

---

# المبادئ المعتمدة

- Modular Architecture
- Separation of Concerns
- Single Responsibility Principle (SRP)
- Open/Closed Principle (OCP)
- Dependency Injection
- Aspect Oriented Programming (AOP)
- Dynamic Authorization
- Extensible Permission Engine
- Domain Driven Modular Design

---

# الوضع الحالي

تم الانتهاء من:

- ✅ Authentication
- ✅ JWT
- ✅ Users Module
- ✅ Mosques Module
- ✅ Mosque Positions
- ✅ Mosque Memberships
- ✅ Permission Groups
- ✅ Permissions
- ✅ Position Permissions
- ✅ User Permissions
- ✅ Dynamic Authorization Engine
- ✅ Authorization Providers
- ✅ Authorization Registry
- ✅ Permission Aspect
- ✅ Automatic Founder Membership
- ✅ CurrentUserService
- ✅ Authorization Testing

---

# المرحلة القادمة

اليوم السادس سيركز على:

- Association Module

ثم إعادة استخدام نفس محرك الصلاحيات دون أي تعديل على البنية الحالية.

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 6 ✅
#### ##### #### ##### #### ##### #### ##### #### #####


#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 7 ✅
#### ##### #### ##### #### ##### #### ##### #### #####


#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 8 ✅
#### ##### #### ##### #### ##### #### ##### #### #####


#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 9 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 10 ✅
#### ##### #### ##### #### ##### #### ##### #### #####