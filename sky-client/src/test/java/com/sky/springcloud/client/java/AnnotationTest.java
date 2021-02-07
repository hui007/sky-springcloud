/**
 * @(#) AnnotationTest.java 1.0 2021-01-05
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.java;

import com.sky.springcloud.client.java.annotation.AnnotationVO;
import com.sky.springcloud.client.java.annotation.MyClassAnnotation;
import com.sky.springcloud.client.java.annotation.MyFieldAnnotation;
import com.sky.springcloud.client.java.annotation.MyMethodAnnotation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 太阿
 * @since 0.1.0
 */
public class AnnotationTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *  解析vo的新旧值
     */
    @Test
    public void testAnnotation() {
        AnnotationVO v1 = new AnnotationVO();
        v1.setColor("red");
        v1.setWeight(1);
        AnnotationVO v2 = new AnnotationVO();
        v2.setColor("green");
        v2.setWeight(2);

        AnnotationMetadata metadata = new StandardAnnotationMetadata(AnnotationVO.class, true);
        assertThat(metadata.isAnnotated(MyClassAnnotation.class.getName())).isTrue();
        assertThat(metadata.isInterface()).isFalse();
        assertThat(metadata.hasAnnotatedMethods(MyMethodAnnotation.class.getName())).isTrue();

        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(MyClassAnnotation.class.getName(), true);
        logger.info("attributes:{}", attributes);
        attributes = metadata.getAllAnnotationAttributes(MyFieldAnnotation.class.getName(), true);
        logger.info("attributes:{}", attributes);

//        AnnotationConfigUtils.
    }
}
