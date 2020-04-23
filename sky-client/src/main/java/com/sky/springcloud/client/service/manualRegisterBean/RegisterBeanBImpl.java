package com.sky.springcloud.client.service.manualRegisterBean;

import org.springframework.stereotype.Service;

@Service("registerBeanBImpl")
public class RegisterBeanBImpl extends AbstractRegisterBean implements RegisterBeanChildB1Inf, RegisterBeanChildB2Inf {
    @Override
    public Class getClientInnerKey() {
        return RegisterBeanChildB1Inf.class;
    }

    @Override
    public Class getClientOuterKey() {
        return RegisterBeanChildB2Inf.class;
    }
}
