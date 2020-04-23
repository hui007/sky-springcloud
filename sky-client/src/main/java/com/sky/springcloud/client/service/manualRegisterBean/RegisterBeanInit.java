package com.sky.springcloud.client.service.manualRegisterBean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RegisterBeanInit implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BeanDefinitionBuilder builder;
        Map<String, RegisterBeanParentInf> beans = applicationContext.getBeansOfType(RegisterBeanParentInf.class);
        for (Map.Entry<String, RegisterBeanParentInf> entry : beans.entrySet()) {
            log.info("beanname:{}", entry.getKey());

            builder = BeanDefinitionBuilder.rootBeanDefinition(RegisterBeanClient.class.getName());
            Map<Class, String> servicesInner = new HashMap<>();
            servicesInner.put(entry.getValue().getClientInnerKey(), entry.getKey());
            builder.addPropertyValue("services", servicesInner);
            builder.addPropertyReference("helper", "registerBeanHelper");
            registry.registerBeanDefinition("registerBeanClient_" + entry.getKey() + "_inner", builder.getBeanDefinition());

            builder = BeanDefinitionBuilder.rootBeanDefinition(RegisterBeanClient.class.getName());
            Map<Class, String> servicesOuter = new HashMap<>();
            servicesOuter.put(entry.getValue().getClientOuterKey(), entry.getKey());
            builder.addPropertyValue("services", servicesOuter);
            builder.addPropertyReference("helper", "registerBeanHelper");
            registry.registerBeanDefinition("registerBeanClient_" + entry.getKey() + "_outer", builder.getBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
