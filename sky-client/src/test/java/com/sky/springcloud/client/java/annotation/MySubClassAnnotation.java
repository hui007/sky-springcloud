package com.sky.springcloud.client.java.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author 太阿
 * @since 0.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MyClassAnnotation
public @interface MySubClassAnnotation {
    @AliasFor(annotation = MyClassAnnotation.class)
    String value() default "";  // 缺省指明继承的父注解的中的属性名称，则默认继承父注解中同名的属性名

    @AliasFor(value = "group", annotation = MyClassAnnotation.class)
    String subGroup() default "";
}
