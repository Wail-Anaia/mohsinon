# DAY 03 - بناء وحدة المساجد (Mosque Module)

**التاريخ:** 2026-07-09

---

# ملخص اليوم

شهد اليوم الثالث أول خطوة حقيقية في تحويل منصة **محسنون** من مجرد مشروع تقني إلى منصة تعكس رسالتها الأساسية.

بعد الانتهاء من تأسيس المشروع وإنشاء نظام المستخدمين في اليومين السابقين، تم تطوير أول وحدة أعمال (Business Module) في المنصة وهي **وحدة المساجد (Mosque Module)**.

أصبح بالإمكان إنشاء مسجد جديد، واستعراض جميع المساجد، والاطلاع على تفاصيل أي مسجد من خلال واجهات REST API، مع اعتماد هيكلية احترافية قابلة للتوسع مستقبلاً.

كما شهد هذا اليوم تحسينات معمارية كبيرة شملت إعادة تنظيم المشروع بالكامل، واعتماد DTO وMapper ونظام استثناءات موحد، مما يجعل المشروع جاهزًا للنمو وإضافة الوحدات المستقبلية دون الحاجة إلى إعادة تصميم.

---

# أهداف اليوم

- إنشاء أول وحدة أعمال (Mosque Module).
- إنشاء كيان المسجد وربطه بقاعدة البيانات.
- إنشاء REST APIs الخاصة بالمساجد.
- اختبار جميع الواجهات البرمجية.
- تحسين البنية الداخلية للمشروع.
- إعادة تنظيم هيكل المشروع وفق أسلوب المشاريع الاحترافية.

---

# ما تم إنجازه

## أولاً: إنشاء Mosque Module

تم إنشاء الوحدة الجديدة:

```text
modules/mosque
```

وتضم:

```text
controller
dto
entity
mapper
repository
service
```

---

## ثانياً: إنشاء الكيان Mosque

تم إنشاء الكيان:

```text
Mosque
```

ويحتوي على الحقول التالية:

- UUID id
- name
- description
- country
- city
- district
- address
- latitude
- longitude
- phone
- email
- verified
- active
- createdAt
- updatedAt

كما تمت إضافة:

- @PrePersist
- @PreUpdate

لإدارة تواريخ الإنشاء والتعديل تلقائيًا.

---

## ثالثاً: إنشاء Repository

تم إنشاء:

```text
MosqueRepository
```

ويرث من:

```java
JpaRepository<Mosque, UUID>
```

---

## رابعاً: إنشاء Service

تم إنشاء:

```text
MosqueService
```

وتوفير الخدمات التالية:

- إنشاء مسجد
- جلب جميع المساجد
- جلب مسجد بواسطة UUID

---

## خامساً: إنشاء Controller

تم إنشاء:

```text
MosqueController
```

وتوفير REST APIs التالية:

### إنشاء مسجد

```http
POST /api/mosques
```

---

### عرض جميع المساجد

```http
GET /api/mosques
```

---

### عرض مسجد واحد

```http
GET /api/mosques/{id}
```

---

## سادساً: اختبار REST APIs

تم اختبار جميع الواجهات باستخدام Postman.

### النتائج

✅ إنشاء مسجد جديد

✅ استرجاع جميع المساجد

✅ استرجاع مسجد واحد

وجميعها نجحت بدون أخطاء.

---

# تحسينات معمارية (Architecture Improvements)

بعد نجاح الوحدة الأساسية تم تحسين البنية بالكامل.

---

## اعتماد DTO

تم إنشاء:

```text
CreateMosqueRequest
```

و

```text
MosqueResponse
```

لفصل واجهات الـ API عن كيانات قاعدة البيانات.

---

## إنشاء Mapper

تم إنشاء:

```text
MosqueMapper
```

لتحويل البيانات بين:

- Entity
- Request DTO
- Response DTO

---

## تحديث Service

أصبحت الخدمة تعمل بالكامل باستخدام DTO بدلاً من Entity.

---

## تحديث Controller

أصبح الـ Controller يستقبل:

```text
CreateMosqueRequest
```

ويرجع:

```text
MosqueResponse
```

---

## إضافة Bean Validation

تم اعتماد:

```java
@NotBlank

@Email

@Size

@Valid
```

للتحقق من صحة البيانات القادمة من العميل.

---

# نظام الاستثناءات (Exception Handling)

تم دمج وحدة المساجد مع نظام الاستثناءات العام للمشروع.

تم إنشاء:

```text
MosqueNotFoundException
```

والذي يرث من:

```text
ResourceNotFoundException
```

بدلاً من استخدام RuntimeException.

كما تم اعتماد:

```text
GlobalExceptionHandler
```

الموجود مسبقًا في المشروع ليصبح مسؤولاً عن معالجة جميع الاستثناءات.

---

# إعادة تنظيم هيكل المشروع

تم تنفيذ أكبر عملية إعادة هيكلة منذ بداية المشروع.

أصبح الهيكل كالتالي:

```text
com.mohsinon
│
├── BackendApplication
│
├── common
│
├── config
│
├── exception
│
├── security
│
└── modules
    │
    ├── user
    │
    └── mosque
```

وتم نقل جميع الملفات إلى الوحدات الجديدة.

---

# الفوائد من إعادة الهيكلة

- سهولة إضافة وحدات جديدة.
- فصل المسؤوليات.
- تحسين قابلية الصيانة.
- تسهيل الاختبارات.
- تنظيم المشروع بطريقة Domain Driven.
- قابلية التوسع على المدى الطويل.

---

# قاعدة البيانات

تم إنشاء جدول:

```text
mosques
```

بنجاح.

كما تم اختبار عمليات:

- INSERT
- SELECT

بنجاح.

---

# APIs المكتملة

| Method | Endpoint | الحالة |
|---------|----------|---------|
| POST | /api/mosques | ✅ |
| GET | /api/mosques | ✅ |
| GET | /api/mosques/{id} | ✅ |

---

# الملفات الجديدة

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

# الملفات التي تم تعديلها

```text
GlobalExceptionHandler.java

pom.xml

MosqueService.java

MosqueController.java
```

---

# أبرز القرارات الهندسية

- عدم ربط الإمام بالمسجد في هذه المرحلة.
- تأجيل نظام الصلاحيات إلى اليوم الرابع.
- استخدام DTO منذ البداية.
- اعتماد Mapper لفصل الطبقات.
- توحيد نظام الاستثناءات.
- اعتماد هيكل Modules.
- استخدام UUID كمفتاح أساسي.
- تصميم الوحدة بحيث تكون قابلة للتوسع دون إعادة كتابة الكود.

---

# التحديات التي تمت معالجتها

- تعارض GlobalExceptionHandler بعد إعادة الهيكلة.
- دمج نظام الاستثناءات القديم مع الوحدة الجديدة.
- نقل جميع الحزم إلى modules.
- تحديث جميع package declarations.
- تحديث جميع import statements.

---

# إحصائيات اليوم

- وحدة جديدة: **1**
- REST APIs: **3**
- DTOs: **2**
- Mapper: **1**
- Repository: **1**
- Service: **1**
- Controller: **1**
- Exception جديد: **1**
- إعادة هيكلة كاملة للمشروع: **نعم**
- اختبارات ناجحة: **100%**

---

# النتيجة النهائية

بنهاية اليوم الثالث أصبحت منصة **محسنون** تمتلك أول وحدة أعمال متكاملة، مع واجهات REST API تعمل بكفاءة، وهيكلية احترافية تعتمد على فصل الطبقات (Layered Architecture) وتنظيم الوحدات (Modular Architecture)، مما يضع أساسًا قويًا لتطوير بقية مكونات المنصة.

أصبح المشروع الآن جاهزًا للانتقال إلى المرحلة التالية، والتي تتضمن ربط المساجد بالمستخدمين، وإدارة الأدوار والصلاحيات، وبناء العلاقات الأساسية التي ستشكل جوهر المنصة.