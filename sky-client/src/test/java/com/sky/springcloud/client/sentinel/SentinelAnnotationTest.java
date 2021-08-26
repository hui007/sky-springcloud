/**
 * @(#) SentinelTest.java 1.0 2021-08-18
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.sentinel;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 参考资料：
 *  https://github.com/alibaba/Sentinel/tree/master/sentinel-demo
 * @author 太阿
 * @date 2021-08-19
 * @since 0.1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SentinelAnnotationTest {
    private RestTemplate restTemplate = new RestTemplate();
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @BeforeClass
    public static void init() {
        // 去掉eureka注册
        System.setProperty("eureka.client.enabled", "false");

        /**
         * 停止/启动远程控制台
         * kill -9 `ps -ef|grep sentinel|awk '$0!~/grep/{print $2}'`
         * ssh joshui@taieout "cd /home/joshui/soft/sentinel/; java -Dserver.port=8180 -Dcsp.sentinel.dashboard.server=localhost:8180 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.2.jar"
         */
    }

    /**
     * 参考：https://sentinelguard.io/zh-cn/docs/annotation-support.html
     * 与控制台的交互没有弄好，注册不到dashboard里
     */
    @Test
    public void testAnnotation() throws InterruptedException {
        // 初始化限流规则
        String setRuleUrl = "http://localhost:8080/initFlowRules/{resource}/{qps}";
        restTemplate.getForObject(setRuleUrl, Void.class, "test", 1);
        restTemplate.getForObject(setRuleUrl, Void.class, "hello", 1);
        restTemplate.getForObject(setRuleUrl, Void.class, "helloAnother", 1);

        // 测试限流
        String fooUrl = "http://localhost:8080/foo?t={long}";
        for (int i = 0; i < 2; i++) {
            HashMap<String, Long> fooMap = new HashMap<>();
            fooMap.put("long", 123L);
            String fooResp = restTemplate.getForObject(fooUrl, String.class, fooMap);
            System.out.println("fooResp：" + fooResp);
        }
        // 测试抛异常
        TimeUnit.MILLISECONDS.sleep(1500); // 休眠1500毫秒
        HashMap<String, Long> fooMap = new HashMap<>();
        fooMap.put("long", -1L);
        String fooResp = restTemplate.getForObject(fooUrl, String.class, fooMap);
        System.out.println("fooResp：" + fooResp);

        // 测试限流
        String bazUrl = "http://localhost:8080/baz/{name}";
        for (int i = 0; i < 2; i++) {
            String bazResp = restTemplate.getForObject(bazUrl, String.class, "taie");
            System.out.println("bazResp：" + bazResp);
        }
        // 测试抛异常
        TimeUnit.MILLISECONDS.sleep(1500); // 休眠1500毫秒
        String bazResp = restTemplate.getForObject(bazUrl, String.class, "bad");
        System.out.println("bazResp：" + bazResp);
    }
}
