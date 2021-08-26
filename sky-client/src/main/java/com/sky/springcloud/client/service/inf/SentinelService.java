package com.sky.springcloud.client.service.inf;

/**
 * @author 太阿
 * @date 2021-08-19
 * @since 0.1.0
 */
public interface SentinelService {
    void test();

    String hello(long s);

    String helloAnother(String name);
}
