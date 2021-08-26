package com.sky.springcloud.client.config;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@Configuration
public class SentinelConfiguration {

    /**
     * 参考：
     *  https://blog.csdn.net/liuhenghui5201/article/details/113839889
     *  https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel里的"@SentinelRestTemplate"
     * 可以不指定每个服务的限流规则，直接使用sentinelRestTemplate来调用每个服务，由sentinelRestTemplate统一处理限流规则
     * @return
     */
    @Bean("sentinelRestTemplate")
    @SentinelRestTemplate
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}