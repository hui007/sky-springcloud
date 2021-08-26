/**
 * @(#) SentinelServiceImpl.java 1.0 2021-08-19
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.sky.springcloud.client.service.inf.SentinelService;
import com.sky.springcloud.client.util.SentinelExceptionUtil;
import org.springframework.stereotype.Service;

/**
 * @author 太阿
 * @date 2021-08-19
 * @since 0.1.0
 */
@Service
public class SentinelServiceImpl implements SentinelService {
    /**
     * 原函数。注解方式埋点不支持 private 方法
     * Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
     * 如果被限流，如果只指定了blockHandler，不会影响业务代码的执行，限流会失效。
     */
    @Override
    @SentinelResource(value = "test", blockHandler = "handleException", blockHandlerClass = {SentinelExceptionUtil.class})
    public void test() {
        System.out.println("Test");
    }

    /**
     * Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
     * 被限流、抛异常之后会走到fallback函数
     * @param s
     * @return
     */
    @Override
    @SentinelResource(value = "hello", fallback = "helloFallback")
    public String hello(long s) {
        if (s < 0) {
            throw new IllegalArgumentException("invalid arg");
        }
        return String.format("Hello at %d", s);
    }

    /**
     * 被限流、抛异常之后会走到defaultFallback函数
     * @param name
     * @return
     */
    @Override
    @SentinelResource(value = "helloAnother", defaultFallback = "defaultFallback",
            exceptionsToIgnore = {IllegalStateException.class})
    public String helloAnother(String name) {
        if (name == null || "bad".equals(name)) {
            throw new IllegalArgumentException("oops");
        }
        if ("foo".equals(name)) {
            throw new IllegalStateException("oops");
        }
        return "Hello, " + name;
    }

    /**
     *
     * @param s
     * @param ex 这个参数不是必须的，可以去掉
     * @return
     */
    public String helloFallback(long s, Throwable ex) {
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at " + s;
    }

    /**
     *
     * @param ex 这个参数不是必须的，可以去掉
     * @return
     */
    public String defaultFallback(Throwable ex) {
        ex.printStackTrace();
        System.out.println("Go to default fallback");
        return "default_fallback";
    }
}
