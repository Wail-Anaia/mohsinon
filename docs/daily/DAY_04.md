# DAY 04 - بناء نظام العضويات والصلاحيات الديناميكية (Authorization Engine)

**التاريخ:** 2026-07-09

---

# 🎯 هدف اليوم

كان الهدف من هذا اليوم تحويل وحدة المساجد من مجرد CRUD إلى نظام إدارة حقيقي، بحيث يصبح لكل مسجد أعضاء، ومناصب، وصلاحيات مستقلة، مع إمكانية إعادة استخدام هذا النظام مستقبلاً في جميع وحدات منصة محسنون.

---

# ✅ ما تم إنجازه

## أولاً: بناء نظام المناصب (Mosque Positions)

تم إنشاء كيان جديد يمثل جميع المناصب داخل المسجد.

### الملفات

```
modules/mosques/entity/MosquePosition.java
modules/mosques/repository/MosquePositionRepository.java
config/seed/MosquePositionSeeder.java
```

---

### المزايا

- تعريف كل منصب بواسطة Code.
- اسم المنصب.
- وصف المنصب.
- إمكانية تعطيله.
- تحديد هل المنصب فريد داخل المسجد (Unique Position).

مثل:

- إمام
- رئيس اللجنة
- نائب الرئيس
- الكاتب
- أمين المال
- عضو اللجنة

---

## ثانياً: Seeder للمناصب

تم إنشاء Seeder يقوم تلقائياً بإضافة جميع المناصب عند تشغيل النظام.

المناصب الحالية:

- IMAM
- COMMITTEE_PRESIDENT
- COMMITTEE_VICE_PRESIDENT
- COMMITTEE_SECRETARY
- COMMITTEE_TREASURER
- COMMITTEE_MEMBER

---

## ثالثاً: بناء نظام العضويات (Mosque Membership)

تم إنشاء كيان يمثل عضوية المستخدم داخل المسجد.

### الملفات

```
MosqueMembership.java
MosqueMembershipRepository.java
```

---

### بيانات العضوية

- المسجد
- المستخدم
- المنصب
- تاريخ البداية
- تاريخ النهاية
- الحالة Active
- من قام بالتعيين
- ملاحظات

---

## رابعاً: إنشاء MosqueMembershipService

تم إنشاء خدمة كاملة لإدارة أعضاء المسجد.

---

### الوظائف المنفذة

#### إضافة عضو جديد

```
assignPosition(...)
```

---

#### تعيين الإمام

```
changeImam(...)
```

---

#### إنهاء العضوية

```
deactivateMembership(...)
```

---

#### عرض جميع أعضاء المسجد

```
getActiveMembers(...)
```

---

#### عرض الإمام الحالي

```
getCurrentImam(...)
```

---

#### عرض جميع الأئمة السابقين

```
getImamHistory(...)
```

---

#### عرض تاريخ مناصب أي مستخدم

```
getUserMembershipHistory(...)
```

---

## خامساً: حماية قواعد العمل (Business Rules)

تم إضافة جميع قواعد العمل المهمة.

---

### منع المستخدم من امتلاك عضويتين نشطتين في نفس المسجد.

---

### منع تكرار المناصب الفريدة.

مثل:

- إمام واحد فقط.
- رئيس لجنة واحد فقط.

---

### عند تغيير الإمام

يتم:

- إنهاء عضوية الإمام القديم.
- حفظ تاريخ النهاية.
- إنشاء عضوية جديدة للإمام الجديد.

وبذلك أصبح لدينا سجل تاريخي كامل.

---

# سادساً: بناء نظام الصلاحيات الديناميكي

تم إنشاء أول محرك صلاحيات حقيقي داخل المنصة.

---

## الجداول الجديدة

### permission_groups

مجموعات الصلاحيات.

مثل:

- MOSQUE
- ASSOCIATION
- PROJECT
- DONATION
- VOLUNTEER
- ADMIN

---

### permissions

تحتوي جميع صلاحيات المنصة.

أمثلة:

```
mosque.view
mosque.update
mosque.assign_imam
mosque.add_member
mosque.remove_member
mosque.manage_committee
mosque.manage_donations
mosque.manage_initiatives
```

---

### position_permissions

تحدد صلاحيات كل منصب.

مثلاً:

الإمام يمتلك:

- mosque.view
- mosque.assign_imam
- mosque.add_member

رئيس اللجنة يمتلك صلاحيات مختلفة.

---

### user_permissions

صلاحيات استثنائية لمستخدم معين.

مثلاً:

إعطاء مستخدم صلاحية

```
mosque.manage_donations
```

بدون تغيير منصبه.

---

# سابعاً: إنشاء Seeders

تم إنشاء Seeders لإضافة البيانات الأساسية تلقائياً.

---

## PermissionGroupSeeder

يقوم بإنشاء جميع مجموعات الصلاحيات.

---

## PermissionSeeder

يقوم بإنشاء جميع الصلاحيات الأساسية.

---

## PositionPermissionSeeder

يقوم بربط الصلاحيات بكل منصب تلقائياً.

---

# ثامناً: إنشاء AuthorizationService

تم إنشاء أول إصدار من محرك الصلاحيات.

يقوم بالتحقق وفق الترتيب التالي:

```
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
Allow / Deny
```

أي عملية أصبحت تعتمد على قاعدة البيانات وليس على أكواد ثابتة.

---

# تاسعاً: ربط الخدمات بمحرك الصلاحيات

تم دمج Authorization داخل خدمة إدارة العضويات.

مثل:

- إضافة عضو.
- تعيين إمام.
- إزالة عضو.

بحيث لا يمكن تنفيذها إلا إذا امتلك المستخدم الصلاحية المناسبة.

---

# عاشراً: تحسين تصميم الخدمة

تم إعادة هيكلة الكود بإضافة دوال خاصة لتجنب التكرار.

مثل:

```
getPositionByCode()

validateUserMembership()

validateUniquePosition()

createMembership()
```

مما جعل الخدمة أكثر تنظيماً وأسهل للصيانة.

---

# 🏗️ البنية الحالية

```
Users
      │
      ▼
Mosque Memberships
      │
      ▼
Mosque Positions
      │
      ▼
Position Permissions
      │
      ▼
Permissions
      │
      ▼
Permission Groups
```

وبالتوازي:

```
Users
      │
      ▼
User Permissions
```

---

# 📊 إحصائيات اليوم

## كيانات جديدة

- MosqueMembership
- MosquePosition
- Permission
- PermissionGroup
- PositionPermission
- UserPermission

---

## Repositories

- MosqueMembershipRepository
- MosquePositionRepository
- PermissionRepository
- PermissionGroupRepository
- PositionPermissionRepository
- UserPermissionRepository

---

## Services

- MosqueMembershipService
- AuthorizationService

---

## Seeders

- MosquePositionSeeder
- PermissionGroupSeeder
- PermissionSeeder
- PositionPermissionSeeder

---

# 🎯 النتيجة

أصبحت وحدة المساجد تمتلك:

- إدارة أعضاء كاملة.
- إدارة المناصب.
- تاريخ الأئمة.
- تاريخ العضويات.
- نظام صلاحيات ديناميكي.
- صلاحيات استثنائية للمستخدمين.
- ربط الصلاحيات بالمناصب.
- إمكانية إعادة استخدام النظام في جميع وحدات المنصة.

---

# نسبة الإنجاز

## المشروع بالكامل

**≈ 18%**

---

## وحدة المساجد

**≈ 95%**

---

## نظام الصلاحيات

**≈ 90%**

---

# مخرجات اليوم

✅ نظام عضويات متكامل.

✅ نظام مناصب ديناميكي.

✅ نظام صلاحيات ديناميكي.

✅ محرك Authorization يعتمد على قاعدة البيانات.

✅ سجل تاريخي للأعضاء والأئمة.

---

# ملاحظات

يُعد هذا اليوم نقطة تحول معمارية في مشروع **محسنون**، حيث تم الانتقال من CRUD تقليدي إلى بنية مؤسسية تعتمد على المناصب والصلاحيات، وهي البنية التي ستُعاد استخدامها لاحقًا في وحدات الجمعيات، المشاريع، المبادرات، التبرعات، والمتطوعين دون الحاجة إلى إعادة تصميمها.