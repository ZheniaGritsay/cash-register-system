package com.projects.model.validation.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Min {
    long value() default 0;

    String message() default "";
}
