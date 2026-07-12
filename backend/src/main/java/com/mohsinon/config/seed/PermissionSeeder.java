package com.mohsinon.config.seed;

import com.mohsinon.modules.authorization.constants.PermissionCodes;
import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PermissionGroup;
import com.mohsinon.modules.authorization.repository.PermissionGroupRepository;
import com.mohsinon.modules.authorization.repository.PermissionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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

    }

    private void seedMosquePermissions() {

        PermissionGroup mosque =
                groupRepository.findByCode("MOSQUE").orElseThrow();

        create(
                mosque,
                PermissionCodes.MOSQUE_VIEW,
                "عرض بيانات المسجد",
                "يسمح بعرض بيانات المسجد"
        );

        create(
                mosque,
                PermissionCodes.MOSQUE_UPDATE,
                "تعديل بيانات المسجد",
                "يسمح بتعديل بيانات المسجد"
        );

        create(
                mosque,
                PermissionCodes.MOSQUE_ASSIGN_IMAM,
                "تعيين الإمام",
                "يسمح بتعيين أو تغيير الإمام"
        );

        create(
                mosque,
                PermissionCodes.MOSQUE_ADD_MEMBER,
                "إضافة عضو",
                "يسمح بإضافة عضو لجنة"
        );

        create(
                mosque,
                PermissionCodes.MOSQUE_REMOVE_MEMBER,
                "إنهاء العضوية",
                "يسمح بإنهاء عضوية عضو"
        );

        create(
                mosque,
                PermissionCodes.MOSQUE_MANAGE_COMMITTEE,
                "إدارة اللجنة",
                "إدارة جميع أعضاء اللجنة"
        );

        create(
                mosque,
                PermissionCodes.MOSQUE_MANAGE_DONATIONS,
                "إدارة التبرعات",
                "إدارة تبرعات المسجد"
        );

        create(
                mosque,
                PermissionCodes.MOSQUE_MANAGE_INITIATIVES,
                "إدارة المبادرات",
                "إدارة المبادرات والمشاريع"
        );

    }
    

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