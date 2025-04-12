package com.wevserver.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@hasPermissionEvaluator.decide(#root, '{permission}', '{targetId}')")
public @interface HasPermission {

    String[] permission() default {};

    String[] targetId() default {};
}
