package com.mohsinon.security.aspect;

import java.lang.reflect.Method;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.mohsinon.modules.authorization.service.AuthorizationService;
import com.mohsinon.modules.users.entity.User;
import com.mohsinon.security.annotation.RequirePermission;
import com.mohsinon.security.annotation.ResourceId;
import com.mohsinon.security.current.CurrentUserService;

@Aspect
@Component
public class PermissionAspect {

    private final AuthorizationService authorizationService;
    private final CurrentUserService currentUserService;

    public PermissionAspect(
            AuthorizationService authorizationService,
            CurrentUserService currentUserService) {

        this.authorizationService = authorizationService;
        this.currentUserService = currentUserService;
    }

    @Around("@annotation(com.mohsinon.security.annotation.RequirePermission)")
    public Object checkPermission(
            ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature =
                (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        RequirePermission annotation =
                method.getAnnotation(RequirePermission.class);

        String groupCode = annotation.groupCode();
        String permission = annotation.permission();

        UUID resourceId = null;

        Object[] args = joinPoint.getArgs();

        java.lang.annotation.Annotation[][] parameterAnnotations =
                method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {

            for (java.lang.annotation.Annotation annotationItem : parameterAnnotations[i]) {

                if (annotationItem instanceof ResourceId) {

                    resourceId = (UUID) args[i];
                    break;
                }
            }

            if (resourceId != null) {
                break;
            }
        }

        if (resourceId == null) {
            throw new IllegalArgumentException(
                    "@ResourceId parameter not found in method: "
                            + method.getName());
        }

        User currentUser = currentUserService.getCurrentUser();
        
        authorizationService.checkPermission(
                currentUser,
                groupCode,
                resourceId,
                permission
        );

        return joinPoint.proceed();
    }

}