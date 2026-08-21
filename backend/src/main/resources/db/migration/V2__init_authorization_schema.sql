-- =====================================================================
-- MOHSINON DATABASE SCHEMA - MIGRATION V2
-- Module: Authorization & Governance (Baseline)
-- =====================================================================

-- 1. Table: global_roles (Platform-wide global roles)
CREATE TABLE IF NOT EXISTS global_roles (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_global_roles_name UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS idx_global_roles_name ON global_roles(name);

-- 2. Table: user_global_roles (N:N Mapping of Users to Global Roles)
CREATE TABLE IF NOT EXISTS user_global_roles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    assigned_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    assigned_by UUID,
    CONSTRAINT uk_user_global_role UNIQUE (user_id, role_id),
    CONSTRAINT fk_ugr_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ugr_role FOREIGN KEY (role_id) REFERENCES global_roles(id) ON DELETE CASCADE,
    CONSTRAINT fk_ugr_assigned_by FOREIGN KEY (assigned_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_user_global_roles_user ON user_global_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_global_roles_role ON user_global_roles(role_id);

-- 3. Table: memberships (Contextual Organizational Memberships e.g. Mosque)
CREATE TABLE IF NOT EXISTS memberships (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    resource_type VARCHAR(50) NOT NULL,
    resource_id UUID NOT NULL,
    membership_role VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    expires_at TIMESTAMP WITH TIME ZONE,
    assigned_by UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_membership_unique UNIQUE (user_id, resource_type, resource_id, membership_role),
    CONSTRAINT fk_memberships_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_memberships_assigned_by FOREIGN KEY (assigned_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_memberships_user ON memberships(user_id);
CREATE INDEX IF NOT EXISTS idx_memberships_resource ON memberships(resource_type, resource_id);
CREATE INDEX IF NOT EXISTS idx_memberships_active ON memberships(user_id, status);

-- 4. Initial Seed for Global Roles
INSERT INTO global_roles (id, name, description, created_at, updated_at, version) VALUES
    ('a0000000-0000-0000-0000-000000000001', 'ROLE_USER', 'Standard registered community user', NOW(), NOW(), 0),
    ('a0000000-0000-0000-0000-000000000002', 'ROLE_VOLUNTEER', 'Registered active volunteer', NOW(), NOW(), 0),
    ('a0000000-0000-0000-0000-000000000003', 'ROLE_DONOR', 'Verified community donor', NOW(), NOW(), 0),
    ('a0000000-0000-0000-0000-000000000004', 'ROLE_ADMIN', 'Platform super administrator', NOW(), NOW(), 0)
ON CONFLICT (name) DO NOTHING;
