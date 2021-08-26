/**
 * @(#) SentinelController.java 1.0 2021-08-19
 * Copyright (c) 2021, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sky.springcloud.client.controller;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.sky.springcloud.client.service.inf.SentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-08-19
 */
@RestController
public class SentinelController {
    @Autowired
    private SentinelService service;

    @GetMapping("/initFlowRules/{resource}/{qps}")
    public void initFlowRules(@PathVariable("resource") String resource, @PathVariable("qps") int qps) throws Exception {
        setFlowRules(resource, qps);
    }

    @GetMapping("/foo")
    public String apiFoo(@RequestParam(required = false) Long t) throws Exception {
        if (t == null) {
            t = System.currentTimeMillis();
        }
        service.test();
        return service.hello(t);
    }

    @GetMapping("/baz/{name}")
    public String apiBaz(@PathVariable("name") String name) {
        return service.helloAnother(name);
    }

    private static void setFlowRules(String resource, int qps){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource(resource);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS.
        rule.setCount(qps);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}
