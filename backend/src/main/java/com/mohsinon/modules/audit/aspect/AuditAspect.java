package com.mohsinon.modules.audit.aspect;

import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.mohsinon.modules.audit.annotation.Audit;
import com.mohsinon.modules.audit.model.AuditableResource;
import com.mohsinon.modules.audit.service.AuditService;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.security.current.CurrentUserService;

@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;

    private final CurrentUserService currentUserService;

    public AuditAspect(
            AuditService auditService,
            CurrentUserService currentUserService) {

        this.auditService = auditService;
        this.currentUserService = currentUserService;
    }

    @Around("@annotation(audit)")
    public Object around(
            ProceedingJoinPoint joinPoint,
            Audit audit) throws Throwable {

        Object result = joinPoint.proceed();

        User actor =
                currentUserService.getCurrentUser();

        UUID entityId = null;

        if (result instanceof AuditableResource auditable) {
            entityId = auditable.getAuditEntityId();
        }

        auditService.log(

                actor,

                audit.entity(),

                audit.action(),

                joinPoint.getArgs(),

                result);

        return result;
    }

}