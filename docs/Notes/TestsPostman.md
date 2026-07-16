#### ##### #### ##### #### ##### #### ##### #### ##### 
### Day 9 ✅ 
#### ##### #### ##### #### ##### #### ##### #### #####

ممتاز، بما أن التسجيل والدخول أصبحا يعملان، وأنجزنا **Shared Query Layer** (Pagination + Search + Filtering)، فهذه أفضل مجموعة اختبارات للتأكد من أن البنية الجديدة تعمل بشكل صحيح قبل تعميمها على بقية الوحدات.

---

# 1. تسجيل مستخدم جديد

**العنوان:** Register New User

```http
POST /api/v1/auth/register
```

✅ المتوقع:

* 201 Created أو 200 OK
* إنشاء المستخدم
* ربطه بدور USER

---

# 2. تسجيل الدخول

**العنوان:** Login

```http
POST /api/v1/auth/login
```

✅ المتوقع:

* JWT Token
* يمكن استخدامه في Authorization

---

# 3. إنشاء مسجد

**العنوان:** Create Mosque

```http
POST /api/v1/mosques
```

✅ المتوقع:

* إنشاء المسجد
* إنشاء Founder Membership
* نجاح Authorization

---

# 4. جلب جميع المساجد

**العنوان:** Get All Mosques

```http
GET /api/v1/mosques
```

✅ المتوقع:

إرجاع:

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

---

# 5. Pagination

**العنوان:** Pagination

```http
GET /api/v1/mosques?page=0&size=5
```

✅ المتوقع

أول خمس نتائج.

---

# 6. الصفحة الثانية

**العنوان:** Second Page

```http
GET /api/v1/mosques?page=1&size=5
```

---

# 7. تغيير حجم الصفحة

**العنوان:** Custom Page Size

```http
GET /api/v1/mosques?size=20
```

---

# 8. ترتيب تصاعدي

**العنوان:** Sort Ascending

```http
GET /api/v1/mosques?sortBy=name&direction=asc
```

---

# 9. ترتيب تنازلي

**العنوان:** Sort Descending

```http
GET /api/v1/mosques?sortBy=name&direction=desc
```

---

# 10. البحث بالمدينة

**العنوان:** Search by City

```http
GET /api/v1/mosques?city=Meknes
```

---

# 11. البحث بالدولة

**العنوان:** Search by Country

```http
GET /api/v1/mosques?country=Morocco
```

---

# 12. البحث بالاسم

**العنوان:** Search by Name

```http
GET /api/v1/mosques?name=مسجد
```

---

# 13. LIKE Search

**العنوان:** Contains Search

```http
GET /api/v1/mosques?name=~محمد
```

---

# 14. Greater Than

**العنوان:** Greater Than Filter

```http
GET /api/v1/mosques?createdAt=>2026-01-01
```

---

# 15. Less Than

**العنوان:** Less Than Filter

```http
GET /api/v1/mosques?createdAt=<2027-01-01
```

---

# 16. Boolean Filter

**العنوان:** Active Mosques

```http
GET /api/v1/mosques?active=true
```

---

# 17. Multiple Filters

**العنوان:** Multiple Filters

```http
GET /api/v1/mosques?country=Morocco&city=Meknes&active=true
```

---

# 18. Pagination + Search

**العنوان:** Search with Pagination

```http
GET /api/v1/mosques?page=0&size=10&city=Meknes
```

---

# 19. Pagination + Sort

**العنوان:** Sort with Pagination

```http
GET /api/v1/mosques?page=0&size=10&sortBy=name&direction=asc
```

---

# 20. Full Query Test

**العنوان:** Full Query Test

```http
GET /api/v1/mosques?page=0&size=10&sortBy=name&direction=asc&country=Morocco&city=Meknes&active=true
```

---

# 21. الحصول على مسجد بالمعرف

**العنوان:** Find Mosque By Id

```http
GET /api/v1/mosques/{id}
```

---

# 22. تعديل مسجد

**العنوان:** Update Mosque

```http
PUT /api/v1/mosques/{id}
```

---

# 23. حذف مسجد

**العنوان:** Delete Mosque

```http
DELETE /api/v1/mosques/{id}
```

---

# 24. اختبار الصلاحيات

**العنوان:** Authorization Test

حاول الوصول إلى:

```http
POST /api/v1/mosques
```

باستخدام مستخدم لا يملك الصلاحية.

✅ المتوقع:

```http
403 Forbidden
```

---

# 25. JWT Test

**العنوان:** JWT Validation

أرسل طلبًا بدون Token.

✅ المتوقع:

```http
401 Unauthorized
```

---

## بعد نجاح هذه الاختبارات

إذا نجحت هذه المجموعة، فهذا يعني أن **النواة المشتركة (Shared Infrastructure)** أصبحت مستقرة، وتشمل:

* ✅ Authentication
* ✅ Authorization
* ✅ JWT
* ✅ Shared Query Layer (Pagination, Sorting, Filtering)
* ✅ Dynamic Specifications
* ✅ Modular Architecture
* ✅ Mosque Module

وعندها نكون جاهزين للانتقال إلى **المرحلة التالية من الـ Infrastructure**، وهي **ModuleInitializer** ثم **Swagger/OpenAPI**، قبل تعميم طبقة البحث والاستعلام على وحدات التبرعات، والمستخدمين، وبقية وحدات منصة **محسنون**.


نعم، وهناك اختبارات مهمة جدًا لم نجربها بعد. في الحقيقة، ما اختبرته يغطي الوظائف الأساسية، لكن إذا كنت تريد أن تكون **النواة (Core Infrastructure)** جاهزة لإعادة الاستخدام في جميع Modules، فأنا أوصي بمجموعة اختبارات إضافية.

---

# المجموعة A — حالات الحافة (Edge Cases)

## 26. البحث بدون نتائج

**العنوان:** Empty Result

```http
GET /api/v1/mosques?city=Tokyo
```

المتوقع:

```json
{
  "content": [],
  "totalElements": 0
}
```

---

## 27. معرف غير موجود

**العنوان:** Find Non Existing Mosque

```http
GET /api/v1/mosques/{random-uuid}
```

المتوقع:

```
404 Not Found
```

---

## 28. تعديل مسجد غير موجود

**العنوان:** Update Non Existing Mosque

```http
PUT /api/v1/mosques/{random-uuid}
```

المتوقع:

```
404 Not Found
```

---

## 29. حذف مسجد غير موجود

**العنوان:** Delete Non Existing Mosque

```http
DELETE /api/v1/mosques/{random-uuid}
```

المتوقع:

```
404 Not Found
```

---

# المجموعة B — اختبارات Pagination

## 30. صفحة بعيدة

```http
GET /api/v1/mosques?page=999
```

المتوقع:

```
content=[]
```

وليس Exception.

---

## 31. حجم صفحة = 1

```http
GET /api/v1/mosques?size=1
```

---

## 32. حجم صفحة كبير

```http
GET /api/v1/mosques?size=100
```

---

# المجموعة C — Sorting

## 33. ترتيب حسب createdAt

```http
GET /api/v1/mosques?sortBy=createdAt
```

---

## 34. ترتيب حسب المدينة

```http
GET /api/v1/mosques?sortBy=city
```

---

## 35. ترتيب بحقل غير موجود

```http
GET /api/v1/mosques?sortBy=xxxxx
```

المتوقع:

```
400 Bad Request
```

وليس

```
500
```

وهذه نقطة مهمة لتحسين معالجة الأخطاء.

---

# المجموعة D — LIKE

## 36.

```http
GET /api/v1/mosques?name=~mos
```

---

## 37.

```http
GET /api/v1/mosques?city=~mek
```

---

# المجموعة E — المقارنات

## 38.

```http
GET /api/v1/mosques?createdAt=>2026-01-01
```

---

## 39.

```http
GET /api/v1/mosques?createdAt=<2030-01-01
```

---

## 40.

```http
GET /api/v1/mosques?verified=true
```

---

## 41.

```http
GET /api/v1/mosques?deleted=false
```

إذا كنت تسمح للمستخدم بإرسال هذا الفلتر.

---

# المجموعة F — Validation

## 42.

إنشاء مسجد بدون اسم

```http
POST /api/v1/mosques
```

Body

```json
{
    "city":"Meknes"
}
```

المتوقع:

```
400 Bad Request
```

---

## 43.

اسم طويل جدًا

---

## 44.

Email غير صالح

---

## 45.

Latitude خارج المجال

---

# المجموعة G — Security

## 46.

Token منتهي الصلاحية

المتوقع

```
401
```

---

## 47.

JWT غير صالح

```
Bearer abcdef
```

---

## 48.

بدون Authorization Header

```
401
```

---

## 49.

صلاحية غير كافية

```
403
```

---

# المجموعة H — Soft Delete

هذه أهم مجموعة الآن.

## 50.

احذف المسجد.

```
DELETE
```

---

## 51.

نفذ

```
GET /api/v1/mosques
```

يجب ألا يظهر المسجد.

---

## 52.

استرجاع

```
PATCH /api/v1/mosques/{id}/restore
```

---

## 53.

نفذ

```
GET
```

يجب أن يظهر المسجد مرة أخرى.

---

# المجموعة I — Performance

## 54.

أنشئ 100 مسجد.

ثم

```
GET /api/v1/mosques?page=0&size=20
```

تأكد من أن الاستجابة تبقى سريعة.

---

# المجموعة J — Infrastructure

## 55.

أنشئ مسجدًا.

ثم نفذ

```
GET
```

ثم

```
PUT
```

ثم

```
DELETE
```

وتحقق من أن الحقول التالية تتغير بالشكل الصحيح:

* `createdAt`
* `updatedAt`
* `deleted`
* `deletedAt`

---

## تقييم المرحلة الحالية

إذا نجحت هذه الاختبارات، فستكون قد أنجزت عمليًا أول نسخة مستقرة من البنية الأساسية (Core Infrastructure v1) لمنصة **محسنون**. عندها ستكون جاهزًا للانتقال إلى المرحلة التالية، وهي **Module Initializer** ثم **Swagger/OpenAPI**، وبعدها يمكن تعميم طبقة البحث والاستعلام والبنية المشتركة على جميع الوحدات مثل **Donations** و**Volunteers** و**Users** دون إعادة كتابة هذه المكونات.


=============================================================================

| العملية            | Endpoint                                     |
| ------------------ | -------------------------------------------- |
| تفعيل              | `PATCH /api/v1/mosques/{id}/activate`        |
| تعطيل              | `PATCH /api/v1/mosques/{id}/deactivate`      |
| أرشفة              | `PATCH /api/v1/mosques/{id}/archive`         |
| استرجاع من الأرشيف | `PATCH /api/v1/mosques/{id}/restore-archive` |
| حذف منطقي          | `DELETE /api/v1/mosques/{id}`                |
| استرجاع المحذوف    | `PATCH /api/v1/mosques/{id}/restore`         |
