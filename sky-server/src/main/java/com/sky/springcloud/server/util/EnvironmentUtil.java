package com.sky.springcloud.server.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentUtil {
    @Autowired
    private Environment environment;
    @Value("${server.port}")
    private String serverPort;
//    @LocalServerPort
//    private String localServerPort2;

    public String getLocalServerPort(){
        return environment.getProperty("local.server.port");
    }
}
