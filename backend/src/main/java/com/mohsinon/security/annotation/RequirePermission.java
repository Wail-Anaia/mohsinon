package com.mohsinon.security.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * Permission Group Code
     *
     * Examples:
     * mosque
     * association
     * project
     */
    String groupCode();

    /**
     * Permission Code
     *
     * Examples:
     * add_member
     * assign_imam
     * delete
     * update
     */
    String permission();

}