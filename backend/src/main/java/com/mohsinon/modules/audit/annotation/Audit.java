package com.mohsinon.modules.audit.annotation;

import com.mohsinon.modules.audit.model.AuditAction;
import com.mohsinon.modules.audit.model.AuditEntityType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Audit {

    AuditAction action();

    AuditEntityType entity();

}