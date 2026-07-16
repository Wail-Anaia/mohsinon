package com.mohsinon.config.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.constants.PermissionCodes;
import com.mohsinon.modules.authorization.constants.PermissionGroupCodes;
import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PermissionGroup;
import com.mohsinon.modules.authorization.repository.PermissionGroupRepository;
import com.mohsinon.modules.authorization.repository.PermissionRepository;

@Component
@Order(3)
public class PermissionSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final PermissionGroupRepository groupRepository;

    public PermissionSeeder(
            PermissionRepository permissionRepository,
            PermissionGroupRepository groupRepository) {

        this.permissionRepository = permissionRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public void run(String... args) {

        seedMosquePermissions();
        seedDonationPermissions();
    }

    // ==========================================================
    // MOSQUE
    // ==========================================================

    private void seedMosquePermissions() {

        PermissionGroup mosque =
                groupRepository.findByCode(PermissionGroupCodes.MOSQUE)
                        .orElseThrow();

        create(
                mosque,
                PermissionCodes.MOSQUE_VIEW,
                "عرض بيانات المسجد",
                "يسمح بعرض بيانات المسجد");

        create(
                mosque,
                PermissionCodes.MOSQUE_UPDATE,
                "تعديل بيانات المسجد",
                "يسمح بتعديل بيانات المسجد");

        create(
                mosque,
                PermissionCodes.MOSQUE_ASSIGN_IMAM,
                "تعيين الإمام",
                "يسمح بتعيين أو تغيير الإمام");

        create(
                mosque,
                PermissionCodes.MOSQUE_ADD_MEMBER,
                "إضافة عضو",
                "يسمح بإضافة عضو لجنة");

        create(
                mosque,
                PermissionCodes.MOSQUE_REMOVE_MEMBER,
                "إنهاء العضوية",
                "يسمح بإنهاء عضوية عضو");

        create(
                mosque,
                PermissionCodes.MOSQUE_MANAGE_COMMITTEE,
                "إدارة اللجنة",
                "إدارة جميع أعضاء اللجنة");

        create(
                mosque,
                PermissionCodes.MOSQUE_MANAGE_DONATIONS,
                "إدارة التبرعات",
                "إدارة تبرعات المسجد");

        create(
                mosque,
                PermissionCodes.MOSQUE_MANAGE_INITIATIVES,
                "إدارة المبادرات",
                "إدارة المبادرات والمشاريع");
    }

    // ==========================================================
    // DONATION
    // ==========================================================

    private void seedDonationPermissions() {

        PermissionGroup donation =
                groupRepository.findByCode(PermissionGroupCodes.DONATION)
                        .orElseThrow();

        create(
                donation,
                PermissionCodes.DONATION_VIEW,
                "عرض التبرعات",
                "يسمح بعرض التبرعات");

        create(
                donation,
                PermissionCodes.DONATION_CREATE,
                "إنشاء تبرع",
                "يسمح بإنشاء تبرع");

        create(
                donation,
                PermissionCodes.DONATION_UPDATE,
                "تعديل التبرع",
                "يسمح بتعديل التبرع");

        create(
                donation,
                PermissionCodes.DONATION_DELETE,
                "حذف التبرع",
                "يسمح بحذف التبرع");

        create(
                donation,
                PermissionCodes.DONATION_RECEIVE,
                "استلام التبرع",
                "يسمح باستلام التبرعات");

        create(
                donation,
                PermissionCodes.DONATION_ALLOCATE,
                "تخصيص التبرع",
                "يسمح بتخصيص التبرعات");

        create(
                donation,
                PermissionCodes.DONATION_DELIVER,
                "تسليم التبرع",
                "يسمح بتسليم التبرعات");

        create(
                donation,
                PermissionCodes.DONATION_CANCEL,
                "إلغاء التبرع",
                "يسمح بإلغاء التبرعات");

        create(
                donation,
                PermissionCodes.DONATION_CATEGORY_VIEW,
                "عرض أصناف التبرعات",
                "يسمح بعرض أصناف التبرعات");

        create(
                donation,
                PermissionCodes.DONATION_CATEGORY_CREATE,
                "إنشاء صنف تبرع",
                "يسمح بإنشاء صنف تبرع");

        create(
                donation,
                PermissionCodes.DONATION_CATEGORY_UPDATE,
                "تعديل صنف تبرع",
                "يسمح بتعديل صنف تبرع");

        create(
                donation,
                PermissionCodes.DONATION_CATEGORY_DELETE,
                "حذف صنف تبرع",
                "يسمح بحذف صنف تبرع");
    }

    // ==========================================================
    // COMMON
    // ==========================================================

    private void create(
            PermissionGroup group,
            String code,
            String name,
            String description) {

        if (permissionRepository.existsByCode(code))
            return;

        Permission permission = new Permission();

        permission.setPermissionGroup(group);
        permission.setCode(code);
        permission.setName(name);
        permission.setDescription(description);

        permissionRepository.save(permission);
    }

}