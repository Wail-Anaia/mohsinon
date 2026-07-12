package com.mohsinon.config.seed;

import com.mohsinon.modules.authorization.constants.PermissionGroupCodes;
import com.mohsinon.modules.authorization.entity.PermissionGroup;
import com.mohsinon.modules.authorization.repository.PermissionGroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class PermissionGroupSeeder implements CommandLineRunner {

    private final PermissionGroupRepository repository;

    public PermissionGroupSeeder(PermissionGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        create(PermissionGroupCodes.MOSQUE, "إدارة المساجد");
        create(PermissionGroupCodes.ASSOCIATION, "الجمعيات");
        create(PermissionGroupCodes.PROJECT, "المشاريع");
        create(PermissionGroupCodes.DONATION, "التبرعات");
        create(PermissionGroupCodes.VOLUNTEER, "التطوع");
        create(PermissionGroupCodes.EVENT, "الفعاليات");
        create(PermissionGroupCodes.COURSE, "الدورات");
        create(PermissionGroupCodes.MARKETPLACE, "السوق");
        create(PermissionGroupCodes.ADMIN, "الإدارة");

    }

    private void create(String code, String name) {

        if (repository.existsByCode(code))
            return;

        PermissionGroup group = new PermissionGroup();

        group.setCode(code);
        group.setName(name);

        repository.save(group);

    }

}