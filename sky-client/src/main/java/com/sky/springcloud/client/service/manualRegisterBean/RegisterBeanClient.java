package com.sky.springcloud.client.service.manualRegisterBean;

import java.util.HashMap;
import java.util.Map;

public class RegisterBeanClient {
    private RegisterBeanHelper helper;
    private Map<Class, String> services = new HashMap<>();

    public Map<Class, String> getServices() {
        return services;
    }

    public void setServices(Map<Class, String> services) {
        this.services = services;
    }

    public RegisterBeanHelper getHelper() {
        return helper;
    }

    public void setHelper(RegisterBeanHelper helper) {
        this.helper = helper;
    }
}
