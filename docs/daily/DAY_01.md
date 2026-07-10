# DAY 01 - تأسيس مشروع منصة محسنون

**التاريخ:** 2026-07-07

---

# الهدف من اليوم

كان الهدف من اليوم هو **تأسيس المشروع بطريقة احترافية** دون كتابة أي منطق برمجي (Business Logic).

لم يكن الهدف إنشاء صفحات أو واجهات أو قواعد بيانات أو APIs، وإنما بناء أساس قوي يمكن الاعتماد عليه طوال عمر المشروع.

---

# أهداف اليوم

* إنشاء مستودع GitHub.
* إنشاء مشروع Backend.
* إنشاء مشروع Frontend.
* إنشاء قاعدة بيانات PostgreSQL.
* تنظيم هيكل المشروع.
* ربط المشروع مع GitHub.
* التأكد من نظافة المستودع.
* تجهيز بيئة التطوير للبدء بالبرمجة.

---

# ما تم إنجازه

## 1. إنشاء مستودع GitHub

تم إنشاء المستودع الرئيسي باسم:

```
mohsinon
```

وهو المستودع الرئيسي الذي سيحتوي على جميع أجزاء المشروع.

---

## 2. إنشاء Backend

تم إنشاء مشروع Spring Boot بالمواصفات التالية:

* Java 21
* Spring Boot
* Maven

### Dependencies

* Spring Web
* Spring Data JPA
* Spring Security
* Validation
* PostgreSQL Driver
* Lombok
* Spring Boot DevTools

---

## 3. إنشاء Frontend

تم إنشاء مشروع Angular Standalone.

الخيارات المستخدمة:

* Standalone Architecture
* Angular Router
* SCSS
* Angular Material

تم تشغيل المشروع بنجاح.

---

## 4. إنشاء قاعدة البيانات

تم إنشاء قاعدة بيانات PostgreSQL باسم:

```
mohsinon_db
```

ولم يتم إنشاء أي جدول في هذا اليوم.

---

# هيكل المشروع النهائي

```
mohsinon/
│
├── .git/
├── .gitignore
├── README.md
│
├── backend/
│
├── frontend/
│
└── docs/
    ├── vision.md
    ├── roadmap.md
    └── requirements.md
```

---

# Backend

تم إنشاء مشروع Maven.

يحتوي على:

```
backend/
│
├── pom.xml
├── mvnw
├── mvnw.cmd
│
└── src
    ├── main
    │
    ├── java
    │   └── com
    │       └── mohsinon
    │           └── BackendApplication.java
    │
    └── resources
        └── application.properties
```

## الملفات

### BackendApplication.java

الوظيفة:

* نقطة تشغيل المشروع.

الدوال الموجودة:

```
main(String[] args)
```

---

### application.properties

ما زال فارغًا تقريبًا.

لم تتم إضافة:

* Database Configuration
* JWT
* Logging
* Security
* JPA Configuration

---

### pom.xml

يحتوي على:

* معلومات المشروع.
* Dependencies.
* Maven Plugins.

---

# Frontend

تم إنشاء مشروع Angular.

الملفات الرئيسية:

```
frontend/
│
├── angular.json
├── package.json
├── package-lock.json
│
├── src
│
│   ├── main.ts
│   ├── index.html
│   ├── styles.scss
│   │
│   └── app
│       ├── app.ts
│       ├── app.html
│       ├── app.scss
│       ├── app.routes.ts
│       ├── app.config.ts
│       └── app.spec.ts
```

---

## app.ts

يحتوي على App Component الافتراضي.

---

## app.routes.ts

حالياً:

```
[]
```

لا توجد صفحات.

---

## app.config.ts

يحتوي على إعدادات Angular الأساسية.

---

## main.ts

يقوم بتشغيل Angular.

---

## styles.scss

ملف التنسيقات العامة.

---

# Git

تم:

* إنشاء Git Repository.
* ربط المشروع مع GitHub.
* إنشاء أول Commit.
* رفع المشروع إلى GitHub.

---

# المشاكل التي واجهتنا

## المشكلة الأولى

Angular أنشأ مستودع Git داخلي داخل:

```
frontend/.git
```

مما جعل Git يعتبر Frontend عبارة عن Submodule.

### الحل

تم حذف Git الداخلي.

ثم إعادة إضافة Frontend كمجلد عادي.

---

## المشكلة الثانية

ظهور:

```
.metadata
```

داخل المشروع.

اتضح أن المشروع تم إنشاؤه داخل Workspace الخاص بـ Eclipse.

### الحل

تم نقل المشروع إلى:

```
C:\Users\Wail Anaia\Projects\mohsinon
```

ليصبح مستقلاً عن Workspace.

---

## المشكلة الثالثة

كان Git لا يزال يعتبر Frontend Submodule.

### الحل

تم تنفيذ:

```
git rm --cached frontend
git add frontend
```

ثم أصبح المشروع طبيعياً.

---

# التحقق النهائي

تم التأكد من:

* عدم وجود Git داخل Frontend.
* عدم وجود Submodules.
* عدم رفع node_modules.
* عدم رفع .angular.
* عدم رفع .metadata.

أصبح Git Repository نظيفًا.

---

# ماذا لم نبرمج؟

لم يتم إنشاء أي:

* Entity
* DTO
* Repository
* Service
* Controller
* API
* JWT
* Security Configuration
* Exception Handler
* Mapper
* Validation
* Tests

وكان ذلك مقصودًا.

---

# لماذا لم نكتب أي كود؟

لأن الهدف كان تأسيس المشروع بطريقة صحيحة.

العديد من المشاريع تفشل بسبب التسرع في كتابة الكود قبل بناء الأساس.

---

# الدروس المستفادة

* وجود مستودع Git واحد فقط للمشروع.
* فصل المشروع عن Workspace الخاص بـ Eclipse.
* عدم رفع الملفات المؤقتة إلى Git.
* تنظيم المشروع قبل البدء بالبرمجة.
* التفكير بالمشروع كمشروع إنتاج (Production) وليس كمشروع تدريبي.

---

# نسبة الإنجاز

| العنصر            | الحالة    |
| ----------------- | --------- |
| GitHub Repository | ✅         |
| Backend           | ✅         |
| Frontend          | ✅         |
| Angular Material  | ✅         |
| PostgreSQL        | ✅         |
| هيكل المشروع      | ✅         |
| Git Repository    | ✅         |
| Business Logic    | ⏳ لم يبدأ |

---

# الخطوة القادمة

اليوم الثاني سيكون بداية البرمجة الفعلية.

سنبدأ ببناء أول Module في المنصة:

```
Users & Authentication
```

وسيتم تنفيذه وفق أفضل الممارسات مع مراعاة:

* Clean Code
* SOLID Principles
* REST API
* DTO Pattern
* Layered Architecture
* Security Best Practices

---

# ملاحظات

يعتبر هذا اليوم يوم التأسيس الرسمي لمنصة محسنون.

تم اتخاذ قرار بأن يتم تطوير المشروع كما لو أنه مشروع إنتاج حقيقي قابل للنمو لسنوات، مع التركيز على جودة التصميم، سهولة الصيانة، وقابلية التوسع، وليس فقط على تشغيل الكود.
