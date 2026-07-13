package com.mohsinon.modules.authorization.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mohsinon.modules.authorization.dto.PermissionGroupRequest;
import com.mohsinon.modules.authorization.dto.PermissionRequest;
import com.mohsinon.modules.authorization.service.PermissionGroupService;
import com.mohsinon.modules.authorization.service.PermissionService;

@Component
@Transactional
public class PermissionInitializer implements CommandLineRunner {

    private final PermissionGroupService permissionGroupService;
    private final PermissionService permissionService;

    public PermissionInitializer(
            PermissionGroupService permissionGroupService,
            PermissionService permissionService) {

        this.permissionGroupService = permissionGroupService;
        this.permissionService = permissionService;
    }

    @Override
    public void run(String... args) {

        initializeDonationPermissions();

        // مستقبلاً
        // initializeVolunteerPermissions();
        // initializeInventoryPermissions();
        // initializeEducationPermissions();
    }

    private void initializeDonationPermissions() {

        createPermissionGroup(
                "donation",
                "Donation Management",
                "Donation module permissions");

        createPermission(
                "donation",
                "donation.view",
                "View Donations");

        createPermission(
                "donation",
                "donation.create",
                "Create Donation");

        createPermission(
                "donation",
                "donation.update",
                "Update Donation");

        createPermission(
                "donation",
                "donation.delete",
                "Delete Donation");

        createPermission(
                "donation",
                "donation.receive",
                "Receive Donation");

        createPermission(
                "donation",
                "donation.allocate",
                "Allocate Donation");

        createPermission(
                "donation",
                "donation.deliver",
                "Deliver Donation");

        createPermission(
                "donation",
                "donation.cancel",
                "Cancel Donation");

        createPermission(
                "donation",
                "donation.category.view",
                "View Donation Categories");

        createPermission(
                "donation",
                "donation.category.create",
                "Create Donation Category");

        createPermission(
                "donation",
                "donation.category.update",
                "Update Donation Category");

        createPermission(
                "donation",
                "donation.category.delete",
                "Delete Donation Category");
    }

    private void createPermissionGroup(
            String code,
            String name,
            String description) {

        try {

            PermissionGroupRequest request =
                    new PermissionGroupRequest();

            request.setCode(code);
            request.setName(name);
            request.setDescription(description);

            permissionGroupService.create(request);

        } catch (Exception ignored) {
            // موجودة مسبقاً
        }
    }

    private void createPermission(
            String groupCode,
            String permissionCode,
            String permissionName) {

        try {

            Long groupId =
                    permissionGroupService
                            .getByCode(groupCode)
                            .getId();

            PermissionRequest request =
                    new PermissionRequest();

            request.setPermissionGroupId(groupId);
            request.setCode(permissionCode);
            request.setName(permissionName);
            request.setDescription(permissionName);

            permissionService.create(request);

        } catch (Exception ignored) {
            // موجودة مسبقاً
        }
    }

}