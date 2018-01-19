package com.projects.model.validation.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DecimalMin {
    double value() default 0.00;

    String message() default "";
}
