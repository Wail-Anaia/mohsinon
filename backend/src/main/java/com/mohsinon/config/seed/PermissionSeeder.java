package com.mohsinon.config.seed;

import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PermissionGroup;
import com.mohsinon.modules.authorization.repository.PermissionGroupRepository;
import com.mohsinon.modules.authorization.repository.PermissionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
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
                "mosque.view",
                "عرض بيانات المسجد",
                "يسمح بعرض بيانات المسجد"
        );

        create(
                mosque,
                "mosque.update",
                "تعديل بيانات المسجد",
                "يسمح بتعديل بيانات المسجد"
        );

        create(
                mosque,
                "mosque.assign_imam",
                "تعيين الإمام",
                "يسمح بتعيين أو تغيير الإمام"
        );

        create(
                mosque,
                "mosque.add_member",
                "إضافة عضو",
                "يسمح بإضافة عضو لجنة"
        );

        create(
                mosque,
                "mosque.remove_member",
                "إنهاء العضوية",
                "يسمح بإنهاء عضوية عضو"
        );

        create(
                mosque,
                "mosque.manage_committee",
                "إدارة اللجنة",
                "إدارة جميع أعضاء اللجنة"
        );

        create(
                mosque,
                "mosque.manage_donations",
                "إدارة التبرعات",
                "إدارة تبرعات المسجد"
        );

        create(
                mosque,
                "mosque.manage_initiatives",
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

        permission.setGroup(group);
        permission.setCode(code);
        permission.setName(name);
        permission.setDescription(description);

        permissionRepository.save(permission);

    }

}