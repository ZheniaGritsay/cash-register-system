package com.projects.model.validation.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Size {
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "";
}
