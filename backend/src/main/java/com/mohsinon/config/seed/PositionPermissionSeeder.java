package com.mohsinon.config.seed;

import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PositionPermission;
import com.mohsinon.modules.authorization.repository.PermissionRepository;
import com.mohsinon.modules.authorization.repository.PositionPermissionRepository;
import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.mosques.repository.MosquePositionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PositionPermissionSeeder implements CommandLineRunner {

    private final MosquePositionRepository positionRepository;
    private final PermissionRepository permissionRepository;
    private final PositionPermissionRepository positionPermissionRepository;

    public PositionPermissionSeeder(
            MosquePositionRepository positionRepository,
            PermissionRepository permissionRepository,
            PositionPermissionRepository positionPermissionRepository) {

        this.positionRepository = positionRepository;
        this.permissionRepository = permissionRepository;
        this.positionPermissionRepository = positionPermissionRepository;
    }

    @Override
    public void run(String... args) {

        imamPermissions();

        committeePresidentPermissions();

        committeeVicePresidentPermissions();

        secretaryPermissions();

        treasurerPermissions();

        committeeMemberPermissions();
    }

    private void imamPermissions() {

        grant("IMAM", "mosque.view");
        grant("IMAM", "mosque.update");
        grant("IMAM", "mosque.assign_imam");
        grant("IMAM", "mosque.add_member");
        grant("IMAM", "mosque.remove_member");
        grant("IMAM", "mosque.manage_committee");
        grant("IMAM", "mosque.manage_donations");
        grant("IMAM", "mosque.manage_initiatives");
    }

    private void committeePresidentPermissions() {

        grant("COMMITTEE_PRESIDENT", "mosque.view");
        grant("COMMITTEE_PRESIDENT", "mosque.update");
        grant("COMMITTEE_PRESIDENT", "mosque.add_member");
        grant("COMMITTEE_PRESIDENT", "mosque.remove_member");
        grant("COMMITTEE_PRESIDENT", "mosque.manage_committee");
        grant("COMMITTEE_PRESIDENT", "mosque.manage_donations");
        grant("COMMITTEE_PRESIDENT", "mosque.manage_initiatives");
    }

    private void committeeVicePresidentPermissions() {

        grant("COMMITTEE_VICE_PRESIDENT", "mosque.view");
        grant("COMMITTEE_VICE_PRESIDENT", "mosque.add_member");
        grant("COMMITTEE_VICE_PRESIDENT", "mosque.manage_committee");
    }

    private void secretaryPermissions() {

        grant("COMMITTEE_SECRETARY", "mosque.view");
    }

    private void treasurerPermissions() {

        grant("COMMITTEE_TREASURER", "mosque.view");
        grant("COMMITTEE_TREASURER", "mosque.manage_donations");
    }

    private void committeeMemberPermissions() {

        grant("COMMITTEE_MEMBER", "mosque.view");
    }

    private void grant(String positionCode, String permissionCode) {

        MosquePosition position =
                positionRepository.findByCode(positionCode).orElseThrow();

        Permission permission =
                permissionRepository.findByCode(permissionCode).orElseThrow();

        if (positionPermissionRepository.existsByPositionAndPermission(position, permission))
            return;

        PositionPermission pp = new PositionPermission();

        pp.setPosition(position);
        pp.setPermission(permission);

        positionPermissionRepository.save(pp);
    }

}