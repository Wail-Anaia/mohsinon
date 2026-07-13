
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

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 10 ✅
#### ##### #### ##### #### ##### #### ##### #### #####

#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 11 ✅
#### ##### #### ##### #### ##### #### ##### #### #####