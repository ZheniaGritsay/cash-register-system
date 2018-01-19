package com.projects.model.validation.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Pattern {
    String regex();

    String message() default "";
}
