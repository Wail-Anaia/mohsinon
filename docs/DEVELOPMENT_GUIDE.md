
#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 8 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# DEVELOPMENT_GUIDE.md

# دليل تطوير منصة محسنون (Mohsinon Platform)

**الإصدار:** 1.0
**آخر تحديث:** 2026-07-13

---

# مقدمة

يهدف هذا الدليل إلى توحيد أسلوب التطوير داخل مشروع **محسنون**، بحيث يتم بناء جميع الوحدات وفق نفس المعايير الهندسية والمعمارية، مما يضمن جودة الكود وسهولة الصيانة والتوسع.

---

# فلسفة المشروع

يعتمد المشروع على المبادئ التالية:

* بناء النواة أولاً (Core First).
* كل وحدة مستقلة (Modular Architecture).
* إعادة استخدام المحركات المشتركة.
* الكود الواضح أهم من الكود المعقد.
* الالتزام بمبادئ SOLID وClean Code.
* عدم تكرار الكود (DRY).

---

# التقنيات المستخدمة

## Backend

* Java 21
* Spring Boot 3.5
* Spring Security
* Spring Data JPA
* Spring Validation
* Spring AOP
* Maven

---

## Database

* PostgreSQL

---

## Frontend

* Angular (Standalone)
* Angular Material
* SCSS

---

## Version Control

* Git
* GitHub

---

# هيكل المشروع

```text
backend
│
├── common
├── config
├── security
├── shared
│
├── modules
│   ├── users
│   ├── auth
│   ├── authorization
│   ├── audit
│   ├── mosques
│   └── donations
│
└── BackendApplication.java
```

---

# إنشاء وحدة جديدة

أي وحدة جديدة يجب أن تتبع نفس الهيكل.

```text
module
│
├── controller
├── service
├── repository
├── entity
├── dto
│   ├── request
│   └── response
├── mapper
├── exception
├── constants
├── model
├── validation
├── dashboard
└── audit
```

لا يتم حذف أي طبقة حتى لو كانت فارغة في البداية، حفاظاً على الاتساق بين الوحدات.

---

# تسمية الحزم

أمثلة:

```text
modules.users

modules.mosques

modules.donations

modules.volunteers

modules.inventory
```

---

# تسمية الكيانات

استخدم أسماء مفردة (Singular).

أمثلة:

```text
User

Mosque

Donation

Volunteer

Campaign
```

ولا تستخدم أسماء جمع مثل:

```text
Users

Mosques

Donations
```

---

# تسمية DTO

## Requests

```text
CreateDonationRequest

UpdateDonationRequest

CreateVolunteerRequest
```

---

## Responses

```text
DonationResponse

VolunteerResponse
```

---

# تسمية Services

```text
DonationService

MosqueService

VolunteerService
```

---

# تسمية Repositories

```text
DonationRepository

MosqueRepository
```

---

# تسمية Controllers

```text
DonationController

MosqueController
```

---

# الكيانات (Entities)

## القواعد

* لا تضع منطق الأعمال داخل Controller.
* لا تضع منطق الأعمال داخل Repository.
* اجعل الكيان مسؤولاً عن دورة حياته.

مثال:

```java
donation.receive();

donation.allocate();

donation.deliver();

donation.cancel();
```

بدلاً من:

```java
donation.setStatus(...);
```

---

# الخدمات (Services)

الخدمة مسؤولة عن:

* تنفيذ حالات الاستخدام (Use Cases).
* استدعاء Repository.
* استخدام Mapper.
* التحقق من وجود الكيانات.
* تطبيق قواعد العمل.

ولا يجب أن تحتوي على منطق خاص بتحويل DTO أو بناء الاستجابات.

---

# المستودعات (Repositories)

يجب أن تحتوي فقط على عمليات الوصول إلى قاعدة البيانات.

مثال:

```java
findByMosque()

findByStatus()

findByCategory()

countByMosque()
```

ويُفضّل استخدام استعلامات العد (`countBy...`) بدلاً من تحميل جميع البيانات ثم حسابها في الذاكرة.

---

# Mapper

كل وحدة تمتلك Mapper خاصاً بها.

المهام:

* Request → Entity
* Entity → Response
* Update Mapping

ولا يتم وضع أي منطق أعمال داخل Mapper.

---

# Controllers

تقتصر مسؤوليات Controller على:

* استقبال الطلب.
* التحقق من صحة البيانات (`@Valid`).
* استدعاء Service.
* إعادة Response.

ولا تحتوي على أي منطق أعمال.

---

# Authorization

أي عملية تحتاج إلى صلاحيات يجب أن تستخدم:

```java
@RequirePermission(
    groupCode = "donation",
    permission = "create"
)
```

ولا يتم تنفيذ التحقق يدوياً داخل الخدمات أو المتحكمات.

---

# Dashboard

لإضافة مؤشرات جديدة:

1. أنشئ Provider جديداً.
2. نفّذ واجهة `MosqueDashboardProvider`.
3. سجّل المزود باستخدام `@Component`.

لا يتم تعديل Dashboard Engine نفسه.

---

# Audit

لإضافة سجل تدقيق جديد:

1. أنشئ Provider جديداً.
2. نفّذ واجهة `AuditDescriptionProvider`.
3. حدّد نوع الكيان والإجراء.
4. أعد وصف العملية.

ولا يتم تعديل `AuditProviderRegistry`.

---

# الاستثناءات

كل وحدة تمتلك استثناءاتها الخاصة.

مثال:

```text
DonationNotFoundException

DonationCategoryAlreadyExistsException

VolunteerNotFoundException
```

وفي المستقبل ستتشارك جميعها أساساً موحداً (`BusinessException`).

---

# الاختبارات

لكل وحدة يجب توفير:

## Unit Tests

* Service Tests
* Mapper Tests

---

## Controller Tests

اختبار REST APIs.

---

## Integration Tests

اختبار السيناريو الكامل.

مثال:

```text
Create Mosque

↓

Create Donation Category

↓

Create Donation

↓

Authorize

↓

Audit

↓

Dashboard
```

---

# معايير جودة الكود

* أسماء واضحة ودلالية.
* دالة واحدة لمسؤولية واحدة.
* تقليل التكرار.
* الاعتماد على حقن التبعيات (Dependency Injection).
* استخدام `final` للحقول المحقونة.
* تجنب الكود غير المستخدم.

---

# إدارة Git

قبل بدء أي Milestone جديد:

```bash
git checkout main
git pull origin main

git checkout -b feature/<milestone-name>
```

بعد الانتهاء:

```bash
git add .
git commit -m "feat: implement donation management foundation"
git push origin feature/<milestone-name>
```

ثم إنشاء Pull Request ودمجه في الفرع الرئيسي بعد المراجعة.

---

# أسلوب الرسائل (Commit Messages)

يعتمد المشروع أسلوب **Conventional Commits**.

أمثلة:

```text
feat: add donation module

feat: implement donation lifecycle

fix: resolve permission validation

refactor: simplify donation mapper

test: add donation integration tests

docs: update architecture documentation

chore: update dependencies
```

---

# دورة التطوير

كل وحدة أعمال تمر بالمراحل التالية:

```text
Planning
    ↓
Entity
    ↓
Repository
    ↓
DTO
    ↓
Mapper
    ↓
Service
    ↓
Controller
    ↓
Authorization
    ↓
Dashboard
    ↓
Audit
    ↓
Tests
    ↓
Documentation
```

---

# التوثيق

عند نهاية كل يوم تطوير يجب تحديث الملفات التالية:

```text
docs/
│
├── daily/
│   └── DAY_XX.md
│
├── PROJECT_STATUS.md
├── CHANGELOG.md
├── ARCHITECTURE.md
├── ROADMAP.md
├── VISION.md
├── REQUIREMENTS.md
├── DECISIONS.md
└── DEVELOPMENT_GUIDE.md
```

ويجب أن يعكس كل ملف الحالة الفعلية للمشروع.

---

# أفضل الممارسات

* اجعل كل وحدة مستقلة قدر الإمكان.
* لا تكسر المحركات المشتركة لإضافة ميزة جديدة.
* فضّل التوسعة (Extension) على التعديل (Modification).
* اجعل الكود قابلاً للاختبار قبل التفكير في تحسين الأداء.
* حسّن الأداء عند الحاجة، لكن لا تُضحِّ بوضوح الكود.
* وثّق كل قرار معماري مؤثر في `DECISIONS.md`.
* حدّث خارطة الطريق بعد كل Milestone.

---

# الخلاصة

أصبح مشروع **محسنون** بعد **Milestone 0.8** يمتلك إطار عمل تطوير واضحاً يمكن تطبيقه على جميع الوحدات المستقبلية. ويضمن هذا الدليل أن يلتزم جميع المطورين بنفس المعايير في تنظيم الكود، وتصميم الوحدات، وآليات التوثيق، والاختبارات، مما يحافظ على جودة المشروع وقابليته للتوسع والصيانة على المدى الطويل.


#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 9 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# دليل التطوير - منصة محسنون (Development Guide)

> آخر تحديث: اليوم التاسع (Day 09)

---

# مقدمة

هذا الدليل يوضح أسلوب التطوير الرسمي داخل مشروع **منصة محسنون**، ويهدف إلى توحيد طريقة كتابة الكود، وهيكل المشروع، وآلية إضافة الوحدات الجديدة، لضمان سهولة الصيانة والتوسع مستقبلاً.

---

# المبادئ الأساسية

يعتمد المشروع على المبادئ التالية:

- Clean Architecture
- SOLID Principles
- Domain Driven Design (DDD) (بشكل مبسط)
- Modular Monolith
- Feature Based Structure
- Separation of Concerns
- Reusable Shared Infrastructure

---

# هيكل المشروع

```
backend/
│
├── common/
├── config/
├── security/
├── shared/
│
├── modules/
│   ├── auth/
│   ├── users/
│   ├── mosques/
│   ├── donations/
│   ├── volunteers/
│   ├── education/
│   ├── marketplace/
│   └── ...
│
└── MohsinonApplication.java
```

---

# هيكل أي Module

كل وحدة يجب أن تتبع نفس البنية.

```
module-name/

├── controller
├── service
├── repository
├── entity
├── dto
│   ├── request
│   └── response
├── mapper
├── exception
├── validation
└── initializer
```

---

# Entity

كل Entity جديد يجب أن يرث من:

```java
LifecycleEntity
```

وليس:

```java
BaseEntity
```

ولا

```java
AuditableEntity
```

لأن LifecycleEntity يحتوي على:

- UUID
- createdAt
- updatedAt
- createdBy
- updatedBy
- active
- archived
- deleted
- archivedAt
- deletedAt
- archivedBy
- deletedBy

---

# إنشاء CRUD جديد

كل وحدة جديدة يجب أن تحتوي على:

- Entity
- Repository
- Service
- Controller
- Mapper
- DTO Request
- DTO Response
- Exception

---

# إنشاء Service

يفضل أن تكون جميع العمليات داخل Service.

مثال:

```java
create()

update()

findById()

search()

delete()

restore()

archive()

restoreArchive()

activate()

deactivate()
```

---

# الحذف

لا يستخدم المشروع:

```java
repository.delete()
```

نهائياً.

بل يستخدم دائماً:

```java
LifecycleService.softDelete()
```

ثم

```java
repository.save()
```

---

# الأرشفة

الأرشفة تتم بواسطة:

```java
LifecycleService.archive()
```

واسترجاعها بواسطة:

```java
LifecycleService.restore()
```

---

# التفعيل والإيقاف

```java
activate()

deactivate()
```

بدلاً من حذف البيانات.

---

# البحث

كل وحدة يجب أن تعتمد على:

```
SearchRequest
```

و

```
SpecificationBuilder
```

و

```
GenericSpecification
```

ولا يتم إنشاء Query مخصصة إلا عند الحاجة.

---

# Pagination

يستخدم المشروع:

```
PaginationUtils
```

لإنشاء:

```
Pageable
```

ولإرجاع:

```
PageResponse
```

---

# Sorting

يتم تمرير:

```
sortBy

direction
```

عن طريق:

```
SearchRequest
```

---

# Filtering

جميع الفلاتر تعتمد على:

```
Map<String,String>
```

ثم تحويلها إلى:

```
SearchCriteria
```

ثم

```
Specification
```

---

# Mapper

لا يتم إرجاع Entity مباشرة.

بل دائماً:

```
Entity

↓

Mapper

↓

Response DTO
```

---

# DTO

أي API يجب أن يستخدم:

```
Request DTO
```

و

```
Response DTO
```

ولا يتم استخدام Entity داخل Controller.

---

# Exceptions

كل Module يمتلك استثناءاته الخاصة.

مثال:

```
MosqueNotFoundException

DonationNotFoundException

PermissionNotFoundException
```

وجميعها ترث من:

```
BusinessException
```

---

# Security

المصادقة تعتمد على:

- JWT

أما الصلاحيات فتعتمد على:

- AuthorizationService
- PermissionResolver
- PermissionCache
- RequirePermission

---

# Authorization

أي Endpoint حساس يجب أن يحتوي على:

```java
@RequirePermission(
    group = "...",
    permission = "..."
)
```

---

# Cache

صلاحيات المستخدم تحفظ داخل:

```
PermissionCache
```

ويتم تنظيفها عند:

- إضافة صلاحية
- حذف صلاحية
- تعديل الصلاحيات

---

# Repository

كل Repository يرث من:

```java
JpaRepository
```

و

```java
JpaSpecificationExecutor
```

عند الحاجة إلى البحث الديناميكي.

---

# Transactions

أي عملية كتابة يجب أن تستخدم:

```java
@Transactional
```

مثل:

- Create
- Update
- Delete
- Restore
- Archive
- Activate

---

# Validation

كل Request يستخدم:

```java
@Valid
```

مع:

```
Jakarta Validation
```

---

# Logging

يعتمد المشروع على Logging الخاص بـ Spring Boot و Hibernate أثناء التطوير، مع إمكانية إضافة SLF4J Logs للعمليات المهمة لاحقًا.

---

# التوثيق

ابتداءً من اليوم العاشر سيتم اعتماد:

- Swagger / OpenAPI

ليصبح جميع الـ APIs موثقة تلقائياً.

---

# Git Workflow

بعد نهاية كل يوم تطوير:

1.

```bash
git status
```

2.

```bash
git add .
```

3.

```bash
git commit -m "Day XX - وصف مختصر"
```

4.

```bash
git push origin main
```

---

# أسلوب كتابة الكود

- أسماء الكلاسات باللغة الإنجليزية.
- أسماء الحقول باللغة الإنجليزية.
- أسماء الـ APIs واضحة ومعبرة.
- تجنب تكرار الكود (DRY).
- الاعتماد على Shared Infrastructure قبل إنشاء حلول خاصة.
- فصل المسؤوليات بين الطبقات.
- كتابة كود قابل لإعادة الاستخدام.

---

# ما تم اعتماده حتى اليوم التاسع

تم اعتماد البنية الأساسية التالية:

- نظام المستخدمين.
- المصادقة باستخدام JWT.
- نظام المساجد.
- نظام المناصب والعضويات.
- نظام الصلاحيات الديناميكي.
- Authorization Framework.
- Shared Query Layer.
- Dynamic Specifications.
- Pagination.
- Sorting.
- Filtering.
- Lifecycle Framework.
- Soft Delete.
- Archive.
- Restore.
- Activate / Deactivate.
- Shared Exceptions.
- Modular Architecture.

---

# الهدف من هذا الدليل

يُعد هذا الدليل المرجع الرسمي للمطورين المشاركين في مشروع **منصة محسنون**، ويضمن توحيد أسلوب التطوير، وتقليل التكرار، والحفاظ على جودة الكود مع نمو المشروع وإضافة وحدات جديدة.

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 10 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

# DEVELOPMENT_GUIDE

**Project:** Mohsinon Platform (منصة محسنون)  
**Version:** Milestone 1.0  
**Last Update:** 2026-07-16

---

# مقدمة

يهدف هذا الدليل إلى توحيد أسلوب تطوير منصة **محسنون**، بحيث يلتزم جميع المطورين بنفس المعايير في كتابة الكود، وتنظيم الملفات، وإدارة Git، وإضافة الوحدات الجديدة.

هذا الدليل هو المرجع الأساسي للمساهمة في المشروع.

---

# المبادئ الأساسية

يلتزم المشروع بالمبادئ التالية:

- Clean Code
- Clean Architecture
- SOLID
- DRY (Don't Repeat Yourself)
- KISS (Keep It Simple)
- Convention over Configuration
- Separation of Concerns

---

# بيئة التطوير

## Backend

- Java 21
- Spring Boot 3.5.x
- Maven
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Swagger / OpenAPI

---

## Frontend

- Angular 21
- TypeScript
- Angular Material
- SCSS
- RxJS

---

## أدوات التطوير

- Eclipse IDE
- Visual Studio Code
- Postman
- Git
- GitHub

---

# هيكل المشروع

```
backend/

├── common/
├── config/
├── security/
├── modules/
├── shared/
└── MohsinonApplication.java
```

---

# إنشاء وحدة جديدة (Module)

يجب أن تتبع كل وحدة نفس الهيكل:

```
modules/

example/

├── controller/
├── service/
├── repository/
├── entity/
├── dto/
│   ├── request/
│   └── response/
├── mapper/
├── specification/
└── exception/
```

يمنع إنشاء بنية مختلفة إلا لسبب معماري واضح.

---

# قواعد الـ Controller

يجب أن يحتوي الـ Controller فقط على:

- استقبال الطلبات.
- التحقق من صحة البيانات.
- استدعاء Service.
- إعادة Response.

ويمنع وضع:

- Business Logic.
- استعلامات قاعدة البيانات.
- عمليات التحويل المعقدة.

---

## مثال

```java
@PostMapping
public ResponseEntity<UserResponse> create(
        @Valid @RequestBody CreateUserRequest request) {

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.create(request));
}
```

---

# قواعد Service

جميع منطق الأعمال (Business Logic) يجب أن يكون داخل Service.

ويشمل:

- Validation
- Authorization
- Transactions
- استدعاء Repository
- تطبيق قواعد العمل

---

# قواعد Repository

يعتمد المشروع على Spring Data JPA.

لا يجوز وضع Business Logic داخل Repository.

---

# قواعد Entity

جميع الكيانات يجب أن ترث من:

```
BaseEntity
```

ويمنع تكرار الحقول التالية:

- id
- createdAt
- updatedAt

---

# قواعد DTO

يجب الفصل بين:

```
Request DTO
```

و

```
Response DTO
```

ولا يجوز استخدام Entity مباشرة داخل واجهات REST.

---

# قواعد Mapper

كل وحدة تمتلك Mapper خاصًا بها.

مثال:

```
UserMapper

MosqueMapper

DonationMapper
```

ويمنع تنفيذ التحويل داخل Controller.

---

# قواعد Exception

جميع الاستثناءات الخاصة بالوحدة توضع داخل:

```
exception/
```

ويجب أن ترث من:

```
BusinessException
```

---

# قواعد Swagger

كل Controller يجب أن يحتوي على:

```java
@Tag
```

وكل Endpoint يجب أن يحتوي على:

```java
@Operation
```

مع استخدام:

```java
@ApiDocumentation
```

بدلاً من كتابة:

```java
@ApiResponses
```

في كل مرة.

---

# قواعد Validation

جميع عمليات التحقق تتم باستخدام Jakarta Validation.

مثال:

```java
@NotBlank

@NotNull

@Email

@Size
```

ويمنع تنفيذ التحقق اليدوي إذا كان بالإمكان استخدام Annotation مناسبة.

---

# قواعد البحث

جميع عمليات البحث يجب أن تعتمد على الطبقة المشتركة.

```
SearchRequest

↓

QueryRequestResolver

↓

SearchService

↓

Specification
```

ولا يجوز تنفيذ Pagination أو Filtering أو Sorting يدويًا داخل الوحدات.

---

# دورة حياة الكيانات

جميع الوحدات التي تدعم إدارة الكيانات يجب أن تستخدم دورة الحياة التالية:

```
Create

↓

Update

↓

Deactivate

↓

Activate

↓

Archive

↓

Restore Archive

↓

Soft Delete

↓

Restore Deleted
```

---

# التوثيق

أي API جديد يجب أن يتضمن:

- @Tag
- @Operation
- @Schema
- @ApiDocumentation

ولا يُقبل دمج واجهات جديدة بدون توثيق Swagger.

---

# التسمية (Naming Convention)

## الأصناف

```
UserService

MosqueController

DonationRepository
```

---

## DTO

```
CreateUserRequest

UpdateUserRequest

UserResponse
```

---

## Exceptions

```
UserNotFoundException

DuplicateEmailException

PermissionDeniedException
```

---

## Services

```
UserService

UserServiceImpl
```

---

# REST API Convention

```
GET

POST

PUT

PATCH

DELETE
```

ويجب أن تكون المسارات واضحة.

مثال:

```
GET /users

GET /users/{id}

POST /users

PUT /users/{id}

DELETE /users/{id}

PATCH /users/{id}/activate
```

---

# Git Workflow

قبل بدء أي تطوير:

```
git pull origin main
```

بعد الانتهاء:

```
git status

git add .

git commit -m "feat: description"

git push origin main
```

---

# أسلوب رسائل Commit

يعتمد المشروع الصيغة التالية:

```
feat:

fix:

refactor:

docs:

test:

style:

perf:

build:

chore:
```

### أمثلة

```
feat: add mosque management

fix: resolve jwt validation issue

docs: update architecture

refactor: simplify authorization layer
```

---

# إضافة ميزة جديدة

قبل كتابة أي كود:

1. تحديد المتطلبات.
2. تصميم قاعدة البيانات.
3. إنشاء Entity.
4. إنشاء Repository.
5. إنشاء Service.
6. إنشاء DTO.
7. إنشاء Mapper.
8. إنشاء Controller.
9. توثيق Swagger.
10. اختبار الميزة.

---

# معايير الجودة

قبل اعتماد أي كود يجب التأكد من:

- عدم وجود أخطاء Compilation.
- عدم وجود تحذيرات مهمة.
- نجاح تشغيل المشروع.
- نجاح اختبار الـ API.
- توثيق Swagger.
- الالتزام بالمعمارية.

---

# اختبار المزايا

قبل إنهاء أي وحدة يجب اختبار:

- Create
- Read
- Update
- Delete
- Search
- Pagination
- Filtering
- Sorting
- Authorization
- Validation
- Error Handling

---

# إدارة التوثيق

في نهاية كل يوم عمل يجب تحديث الملفات التالية:

```
docs/

daily/
    DAY_XX.md

PROJECT_STATUS.md

CHANGELOG.md

ARCHITECTURE.md

ROADMAP.md

VISION.md

REQUIREMENTS.md

DECISIONS.md

DEVELOPMENT_GUIDE.md
```

---

# قواعد مراجعة الكود

قبل دمج أي تغيير تأكد من:

- عدم تكرار الكود.
- استخدام المكونات المشتركة.
- الالتزام بالتسمية.
- عدم كسر الوحدات الأخرى.
- تحديث التوثيق.
- المحافظة على قابلية التوسع.

---

# خارطة التطوير

## المرحلة الحالية

✅ Milestone 1.0 (Backend Foundation)

---

## المرحلة التالية

🚧 Frontend Foundation (Angular)

وتشمل:

- Core
- Shared
- Layouts
- Authentication
- Dashboard
- Routing
- Guards
- Interceptors
- Design System

---

# فلسفة المشروع

يعتمد مشروع **محسنون** على مبدأ بسيط:

> **ابنِ مرة واحدة، وأعد الاستخدام دائمًا.**

لذلك تُفضَّل دائمًا الحلول العامة والقابلة لإعادة الاستخدام على الحلول الخاصة أو المكررة، مع الحفاظ على وضوح الكود وقابليته للصيانة والتوسع.

---

# الخلاصة

يهدف هذا الدليل إلى ضمان أن ينمو المشروع بطريقة منظمة ومتسقة، بحيث تبدو جميع الوحدات وكأنها كُتبت بواسطة فريق واحد، حتى مع توسع المنصة وإضافة مطورين جدد.

الالتزام بهذا الدليل يساهم في الحفاظ على جودة الكود، وسهولة التطوير، واستقرار المنصة على المدى الطويل.

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 11 ✅
#### ##### #### ##### #### ##### #### ##### #### #####