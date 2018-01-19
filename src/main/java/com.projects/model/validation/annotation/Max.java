package com.projects.model.validation.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Max {
    long value() default Long.MAX_VALUE;

    String message() default "";
}
