package com.mohsinon.config.seed;

import com.mohsinon.modules.authorization.entity.PermissionGroup;
import com.mohsinon.modules.authorization.repository.PermissionGroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PermissionGroupSeeder implements CommandLineRunner {

    private final PermissionGroupRepository repository;

    public PermissionGroupSeeder(PermissionGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        create("MOSQUE", "إدارة المساجد");
        create("ASSOCIATION", "الجمعيات");
        create("PROJECT", "المشاريع");
        create("DONATION", "التبرعات");
        create("VOLUNTEER", "التطوع");
        create("EVENT", "الفعاليات");
        create("COURSE", "الدورات");
        create("MARKETPLACE", "السوق");
        create("ADMIN", "الإدارة");

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