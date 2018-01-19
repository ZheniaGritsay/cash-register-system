package com.projects.model.validation.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DecimalMax {
    double value() default Double.MAX_VALUE;

    String message() default "";
}
