package com.sky.springcloud.client.java.annotation;

import java.lang.annotation.*;

/**
 * @author 太阿
 * @since 0.1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyFieldAnnotation {
    /**
     * 颜色枚举
     * @author peida
     *
     */
    public enum Color{ BULE,RED,GREEN};

    /**
     * 颜色属性
     * @return
     */
    Color fruitColor() default Color.GREEN;

    String fieldName() default "";
}
