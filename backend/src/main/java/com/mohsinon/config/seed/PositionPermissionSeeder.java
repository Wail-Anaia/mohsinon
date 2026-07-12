package com.mohsinon.config.seed;

import com.mohsinon.modules.authorization.constants.PermissionCodes;
import com.mohsinon.modules.mosques.constants.MosquePositionCodes;
import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PositionPermission;
import com.mohsinon.modules.authorization.repository.PermissionRepository;
import com.mohsinon.modules.authorization.repository.PositionPermissionRepository;
import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.mosques.repository.MosquePositionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
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

        grant(MosquePositionCodes.IMAM, PermissionCodes.MOSQUE_VIEW);
        grant(MosquePositionCodes.IMAM, PermissionCodes.MOSQUE_UPDATE);
        grant(MosquePositionCodes.IMAM, PermissionCodes.MOSQUE_ASSIGN_IMAM);
        grant(MosquePositionCodes.IMAM, PermissionCodes.MOSQUE_ADD_MEMBER);
        grant(MosquePositionCodes.IMAM, PermissionCodes.MOSQUE_REMOVE_MEMBER);
        grant(MosquePositionCodes.IMAM, PermissionCodes.MOSQUE_MANAGE_COMMITTEE);
        grant(MosquePositionCodes.IMAM, PermissionCodes.MOSQUE_MANAGE_DONATIONS);
        grant(MosquePositionCodes.IMAM, PermissionCodes.MOSQUE_MANAGE_INITIATIVES);
    }
    
    
    private void committeePresidentPermissions() {
    	
    	grant(MosquePositionCodes.COMMITTEE_PRESIDENT, PermissionCodes.MOSQUE_VIEW);
    	grant(MosquePositionCodes.COMMITTEE_PRESIDENT, PermissionCodes.MOSQUE_UPDATE);
        grant(MosquePositionCodes.COMMITTEE_PRESIDENT, PermissionCodes.MOSQUE_ADD_MEMBER);
        grant(MosquePositionCodes.COMMITTEE_PRESIDENT, PermissionCodes.MOSQUE_REMOVE_MEMBER);
        grant(MosquePositionCodes.COMMITTEE_PRESIDENT, PermissionCodes.MOSQUE_MANAGE_COMMITTEE);
        grant(MosquePositionCodes.COMMITTEE_PRESIDENT, PermissionCodes.MOSQUE_MANAGE_DONATIONS);
        grant(MosquePositionCodes.COMMITTEE_PRESIDENT, PermissionCodes.MOSQUE_MANAGE_INITIATIVES);
    }

    private void committeeVicePresidentPermissions() {
    	
    	grant(MosquePositionCodes.COMMITTEE_VICE_PRESIDENT, PermissionCodes.MOSQUE_VIEW);
    	grant(MosquePositionCodes.COMMITTEE_VICE_PRESIDENT, PermissionCodes.MOSQUE_ADD_MEMBER);
    	grant(MosquePositionCodes.COMMITTEE_VICE_PRESIDENT, PermissionCodes.MOSQUE_MANAGE_COMMITTEE);
    }

    private void secretaryPermissions() {

        grant(MosquePositionCodes.COMMITTEE_SECRETARY, PermissionCodes.MOSQUE_VIEW);
    }

    private void treasurerPermissions() {

        grant(MosquePositionCodes.COMMITTEE_TREASURER, PermissionCodes.MOSQUE_VIEW);
        grant(MosquePositionCodes.COMMITTEE_TREASURER, PermissionCodes.MOSQUE_MANAGE_DONATIONS);
    }

    private void committeeMemberPermissions() {

        grant(MosquePositionCodes.COMMITTEE_MEMBER, PermissionCodes.MOSQUE_VIEW);
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