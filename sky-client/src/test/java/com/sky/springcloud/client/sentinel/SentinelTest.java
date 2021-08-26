/**
 * @(#) SentinelTest.java 1.0 2021-08-18
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.junit.Test;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

/**
 * 参考资料：
 *  https://github.com/alibaba/Sentinel/tree/master/sentinel-demo
 * @author 太阿
 * @date 2021-08-18
 * @since 0.1.0
 */
public class SentinelTest {

    /**
     * 没有整合主流框架的默认适配，手动写限流例子，需要单独引入sentinel的几个jar包
     * 参考：https://sentinelguard.io/zh-cn/docs/quick-start.html
     * @throws InterruptedException
     */
    @Test
    public void testApi() throws InterruptedException {
        // 配置规则.
        initFlowRules();

        long current = Clock.systemUTC().millis();
        long gap;
        while ((gap = Clock.systemUTC().millis() - current) < 3000) {
            // 1.5.0 版本开始可以直接利用 try-with-resources 特性
            try (Entry entry = SphU.entry("HelloWorld")) {
                // 被保护的逻辑
                System.out.println("hello world:" + gap/1000);
            } catch (BlockException ex) {
                // 处理被流控的逻辑
                System.out.println("blocked!:" + gap/1000);
            }
        }
    }

    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(20);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}
