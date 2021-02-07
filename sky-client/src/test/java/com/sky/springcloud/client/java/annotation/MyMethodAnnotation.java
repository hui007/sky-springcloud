package com.sky.springcloud.client.java.annotation;

import java.lang.annotation.*;

/**
 * @author 太阿
 * @since 0.1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyMethodAnnotation {
    boolean isIgnore() default false;
}
