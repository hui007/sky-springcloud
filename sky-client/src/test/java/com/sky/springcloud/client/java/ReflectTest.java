/**
 * @(#) ReflectTest.java 1.0 2021-01-08
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.java;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 太阿
 * @since 0.1.0
 */
public class ReflectTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testClassLoad() {
        // class是否存在
        boolean present = ClassUtils.isPresent("com.sky.springcloud.client.java.ReflectTest", null);
        assertThat(present).isTrue();

        // 获取类加载器
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        assertThat(classLoader).isNotNull();

        // 通过classloader加载属性文件
        try {
            Enumeration<URL> urls = (classLoader != null ?
                    classLoader.getResources(SpringFactoriesLoader.FACTORIES_RESOURCE_LOCATION) :
                    ClassLoader.getSystemResources(SpringFactoriesLoader.FACTORIES_RESOURCE_LOCATION));
            MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                UrlResource resource = new UrlResource(url);
                Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                for (Map.Entry<?, ?> entry : properties.entrySet()) {
                    String factoryClassName = ((String) entry.getKey()).trim();
                    for (String factoryName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
                        result.add(factoryClassName, factoryName.trim());
                    }
                }
            }
            logger.info("通过classloader加载属性文件:{}", result);
        }
        catch (IOException ex) {
            logger.error("通过classloader加载属性文件异常", ex);
        }

        // 根据class名称实例化对象
        try {
            Class<?> instanceClass = ClassUtils.forName("com.sky.springcloud.client.java.ReflectTest", classLoader);
            Assert.isAssignable(ReflectTest.class, instanceClass);
            Constructor<?> constructor = instanceClass
                    .getDeclaredConstructor(new Class[]{});
            ReflectTest instance = (ReflectTest) BeanUtils.instantiateClass(constructor, new Object(){});
            assertThat(instance).isNotNull();
        }
        catch (Throwable ex) {
            throw new IllegalArgumentException(
                    "Cannot instantiate " + ReflectTest.class + " : " + "com.sky.springcloud.client.java.ReflectTest", ex);
        }
    }
}
