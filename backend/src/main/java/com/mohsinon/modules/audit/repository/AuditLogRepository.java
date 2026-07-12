package com.mohsinon.modules.audit.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mohsinon.modules.audit.entity.AuditLog;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, UUID> {

}