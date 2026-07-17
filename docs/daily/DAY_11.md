# DAY 11 - تأسيس واجهة منصة محسنون (Angular Foundation)

**التاريخ:** 2026-07-17

---

# ملخص اليوم

بعد الانتهاء من بناء النواة الخلفية (Backend) خلال الأيام العشرة الماضية، بدأنا اليوم المرحلة الأولى من تطوير واجهة منصة **محسنون** باستخدام **Angular 21**.

لم يكن الهدف من هذا اليوم بناء صفحات أو تصميم واجهات، وإنما وضع الأساس المعماري الذي ستبنى عليه جميع شاشات المنصة مستقبلاً.

تم اعتماد نفس الفلسفة التي اتبعناها في الـ Backend:

* Clean Architecture
* Feature-Based Structure
* Separation of Concerns
* Reusable Components
* Scalability
* Maintainability

وبذلك أصبح المشروع جاهزًا لبناء واجهة احترافية قابلة للتوسع.

---

# أهداف اليوم

* مراجعة مشروع Angular الحالي.
* تقييم جاهزية بيئة العمل.
* إعادة تصميم معمارية المشروع.
* إنشاء الهيكل الأساسي للواجهة.
* إعداد طبقة الاتصال مع Backend.
* بناء طبقة المصادقة (Authentication Foundation).
* تجهيز المشروع لبناء أول شاشة حقيقية.

---

# مراجعة المشروع الحالي

تمت مراجعة مشروع Angular الحالي وكانت النتائج كما يلي:

| العنصر                | الحالة |
| --------------------- | ------ |
| Angular 21            | ✅      |
| Standalone Components | ✅      |
| Angular Material      | ✅      |
| SCSS                  | ✅      |
| Routing               | ✅      |
| TypeScript 5.9        | ✅      |
| RxJS                  | ✅      |

تم اعتماد المشروع الحالي دون الحاجة إلى إعادة إنشائه.

---

# إعادة تنظيم هيكل المشروع

تم اعتماد الهيكل التالي:

```text
src/
│
├── app/
│
│   ├── core/
│   │
│   ├── shared/
│   │
│   ├── features/
│   │
│   ├── layouts/
│   │
│   └── routing/
│
├── assets/
│
├── environments/
│
└── styles/
```

---

## Core

تم تخصيص مجلد Core لكل الخدمات المشتركة الخاصة بالنظام.

```text
core/

auth/

config/

guards/

interceptors/

services/

state/
```

---

## Shared

تم إنشاء Shared ليحتوي جميع العناصر القابلة لإعادة الاستخدام.

```text
shared/

components/

constants/

directives/

enums/

interfaces/

models/

pipes/

types/

utils/

validators/
```

---

## Features

تم اعتماد Feature-Based Architecture.

```text
features/

auth/

dashboard/

users/

mosques/

donations/

volunteers/

projects/
```

---

## Layouts

تم تجهيز بنية الـ Layouts.

```text
layouts/

public/

dashboard/

admin/
```

---

# إنشاء Design System

تم الاتفاق على بناء نظام تصميم خاص بمنصة محسنون.

تم إنشاء مجلد:

```text
styles/
```

ويحتوي على:

```text
_variables.scss

_colors.scss

_typography.scss

_spacing.scss

_breakpoints.scss

_mixins.scss

_theme.scss

_animations.scss
```

مع إمكانية إضافة:

```text
base/

components/
```

لاحقًا.

---

# إعداد Environments

تم تجهيز ملفات البيئة:

```text
environment.ts

environment.development.ts
```

وتضم:

* اسم التطبيق.
* إصدار التطبيق.
* عنوان الـ Backend.
* حالة الإنتاج أو التطوير.

---

# إعداد API Configuration

تم إنشاء:

```text
ApiConfig
```

ويحتوي على:

* Base URL
* Auth Endpoints
* Users Endpoints
* Mosques Endpoints
* Donations Endpoints
* Permissions Endpoints

أصبح تغيير عنوان الـ API يتم من مكان واحد فقط.

---

# إنشاء Constants

تم اعتماد مجلد Constants.

تم إنشاء:

```text
StorageConstants
```

لتوحيد مفاتيح Local Storage.

كما تم إنشاء:

```text
RouteConstants
```

لتوحيد جميع مسارات التطبيق.

---

# إنشاء Interfaces

تم إنشاء:

```text
ApiResponse<T>
```

ليكون الأساس الموحد للتعامل مع استجابات الـ Backend.

---

# إعداد HttpClient

تم تجهيز التطبيق لاستخدام:

* provideHttpClient()
* Functional Interceptors

بدلاً من الأسلوب القديم.

---

# إنشاء Auth Interceptor

تم تصميم:

```text
AuthInterceptor
```

ليقوم تلقائيًا بإضافة:

```text
Authorization: Bearer JWT
```

إلى جميع الطلبات المرسلة إلى الـ Backend.

---

# إنشاء Token Service

تم إنشاء:

```text
TokenService
```

ليكون المسؤول الوحيد عن:

* حفظ الـ JWT.
* قراءة الـ JWT.
* حذف الـ JWT.
* التحقق من وجود الـ JWT.

مما يمنع استخدام Local Storage مباشرة داخل الخدمات.

---

# إنشاء HttpService

تم إنشاء خدمة أساسية:

```text
HttpService
```

لتكون الأب لجميع الخدمات المستقبلية.

وبذلك لن يتكرر حقن HttpClient داخل كل Service.

---

# إنشاء Authentication Layer

تم تصميم طبقة المصادقة.

تشمل:

* LoginRequest
* LoginResponse
* CurrentUser
* AuthService

وأصبحت جاهزة للاتصال بالـ Backend.

---

# Current User

تم إنشاء:

```text
CurrentUserService
```

باستخدام:

```text
Angular Signals
```

بدلاً من BehaviorSubject.

وهو الأسلوب الحديث في Angular.

---

# إنشاء Auth Guard

تم إنشاء:

```text
AuthGuard
```

لحماية الصفحات الخاصة بالمستخدمين المسجلين.

---

# Routing

تم تجهيز أول Routing مبدئي للتطبيق تمهيدًا لإضافة:

* Login
* Dashboard

في الأيام القادمة.

---

# الهوية البصرية

تم الاتفاق على أن يكون للمنصة نظام تصميم مستقل.

الألوان الأساسية:

* Emerald Green
* Deep Blue
* White
* Gray

مع دعم:

* Light Theme
* Dark Theme

لاحقًا.

---

# المعمارية المعتمدة

```text
Angular

↓

Layouts

↓

Features

↓

Shared Components

↓

Core Services

↓

HTTP Layer

↓

Spring Boot Backend
```

---

# الإنجازات

✅ مراجعة مشروع Angular.

✅ اعتماد Angular 21 Standalone.

✅ اعتماد Angular Material.

✅ اعتماد Feature-Based Architecture.

✅ إعادة تنظيم المشروع.

✅ إنشاء Core.

✅ إنشاء Shared.

✅ إنشاء Features.

✅ إنشاء Layouts.

✅ إنشاء Design System.

✅ إعداد Environments.

✅ إعداد API Configuration.

✅ إعداد Constants.

✅ إعداد Interfaces.

✅ إعداد HttpClient.

✅ إعداد Auth Interceptor.

✅ إنشاء TokenService.

✅ إنشاء HttpService.

✅ إنشاء AuthService.

✅ إنشاء CurrentUserService.

✅ إنشاء AuthGuard.

✅ تجهيز Routing الأساسي.

---

# ما لم يتم تنفيذه

لم يتم بناء أي صفحة فعلية.

ولم يتم تصميم Login.

ولم يتم إنشاء Dashboard.

وذلك لأن الهدف من اليوم كان بناء البنية الأساسية فقط.

---

# نسبة الإنجاز

| المرحلة                   | النسبة |
| ------------------------- | -----: |
| Frontend Foundation       |   100% |
| Architecture              |   100% |
| Authentication Foundation |   100% |
| Design System Foundation  |    90% |
| Login UI                  |     0% |
| Dashboard UI              |     0% |

---

# مخرجات اليوم

* Frontend Architecture
* Core Layer
* Shared Layer
* Feature Modules
* Layout Structure
* Design System Foundation
* API Configuration
* Environment Configuration
* Authentication Foundation
* HTTP Foundation

---

# الدروس المستفادة

* بناء الواجهة يبدأ من المعمارية وليس من الصفحات.
* وجود Core وShared منذ البداية يمنع الفوضى مع نمو المشروع.
* الاعتماد على Functional APIs في Angular 21 يجعل المشروع أبسط وأكثر حداثة.
* تصميم طبقة Authentication بشكل مستقل يسهل إضافة Refresh Token وميزات الأمن مستقبلًا.
* الاستثمار في البنية الأساسية اليوم سيختصر وقتًا كبيرًا عند تطوير الوحدات القادمة.

---

# خطة اليوم التالي (DAY 12)

سيكون التركيز على أول واجهة حقيقية في المشروع.

تشمل:

1. إنشاء Public Layout.
2. إنشاء Design System الأساسي.
3. إنشاء المكونات المشتركة الأولى.
4. بناء صفحة Login.
5. ربط Login مع Backend.
6. حفظ JWT.
7. الانتقال إلى Dashboard بعد نجاح تسجيل الدخول.
8. إنشاء Dashboard Layout.
9. عرض بيانات المستخدم الحالي.
10. بدء بناء أول تجربة استخدام حقيقية لمنصة محسنون.

---

# خاتمة

اليوم الحادي عشر يمثل نقطة تحول في مشروع **محسنون**.

بعد عشرة أيام من بناء الخدمات والواجهات البرمجية (APIs)، أصبح المشروع يمتلك أساسًا قويًا لواجهة مستخدم حديثة تعتمد على Angular 21، وتلتزم بنفس المبادئ الهندسية التي بُني عليها الـ Backend.

لقد أصبحت المنصة جاهزة للانتقال من مرحلة البنية التحتية إلى مرحلة التفاعل الحقيقي مع المستخدمين، حيث ستبدأ خلال الأيام القادمة أولى الشاشات الفعلية، لتتحول محسنون تدريجيًا إلى منصة متكاملة يمكن استخدامها واختبارها عمليًا.
