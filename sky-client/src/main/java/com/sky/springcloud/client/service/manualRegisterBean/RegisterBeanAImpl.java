package com.sky.springcloud.client.service.manualRegisterBean;

import org.springframework.stereotype.Service;

@Service("registerBeanAImpl")
public class RegisterBeanAImpl extends AbstractRegisterBean implements RegisterBeanChildA1Inf, RegisterBeanChildA2Inf {

    @Override
    public Class getClientInnerKey() {
        return RegisterBeanChildA1Inf.class;
    }

    @Override
    public Class getClientOuterKey() {
        return RegisterBeanChildA2Inf.class;
    }
}
