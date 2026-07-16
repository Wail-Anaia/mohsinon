package com.mohsinon.config.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.mohsinon.modules.users.entity.Role;
import com.mohsinon.modules.users.repository.RoleRepository;

@Component
@Order(5)
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        create("ADMIN", "System Administrator");

        create("USER", "Normal User");

        create("IMAM", "Mosque Imam");

        create("MOSQUE_COMMITTEE", "Mosque Committee Member");

        create("DONOR", "Donation Donor");

        create("VOLUNTEER", "Volunteer");

        create("ASSOCIATION_MANAGER", "Association Manager");

    }

    private void create(
            String name,
            String description) {

        if (roleRepository.existsByName(name)) {
            return;
        }

        Role role = new Role();

        role.setName(name);
        role.setDescription(description);

        roleRepository.save(role);
    }

}