/**
 * @(#) AnnotationVO.java 1.0 2021-01-05
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.java.annotation;

/**
 * @author 太阿
 * @since 0.1.0
 */
@MyClassAnnotation(group = "水果")
public class AnnotationVO {
    @MyFieldAnnotation(fieldName = "水果颜色")
    private String color;
    @MyFieldAnnotation(fieldName = "水果重量")
    private int weight;

    @MyMethodAnnotation(isIgnore = true)
    public void parseAnnotation() {

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
