package com.sky.springcloud.client.controller;

import com.sky.springcloud.client.domain.CommonResult;
import com.sky.springcloud.client.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * 使用ribbon方式请求远程服务
 */
@RestController
@RequestMapping("/user/ribbon")
public class UserRibbonController  implements ApplicationContextAware {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-in-eureka.sky-server-ribbon}")
    private String userServiceUrl;
    private ApplicationContext applicationContext;

    @GetMapping("/{id}")
    public CommonResult getUser(@PathVariable Long id) {
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    @GetMapping("/getByUsername")
    public CommonResult getByUsername(@RequestParam String username) {
        return restTemplate.getForObject(userServiceUrl + "/user/getByUsername?username={1}", CommonResult.class, username);
    }

    @GetMapping("/getEntityByUsername")
    public CommonResult getEntityByUsername(@RequestParam String username) {
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(userServiceUrl + "/user/getByUsername?username={1}", CommonResult.class, username);
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return new CommonResult("操作失败", 500);
        }
    }

    @PostMapping("/create")
    public CommonResult create(@RequestBody User user) {
        return restTemplate.postForObject(userServiceUrl + "/user/create", user, CommonResult.class);
    }

    @PostMapping("/update")
    public CommonResult update(@RequestBody User user) {
        return restTemplate.postForObject(userServiceUrl + "/user/update", user, CommonResult.class);
    }

    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        return restTemplate.postForObject(userServiceUrl + "/user/delete/{1}", null, CommonResult.class, id);
    }

    @GetMapping("/getManualRegisterBean")
    public void getManualRegisterBean() {
        Object aInner = applicationContext.getBean("registerBeanClient_registerBeanAImpl_inner");
        Object aOuter = applicationContext.getBean("registerBeanClient_registerBeanAImpl_outer");
        Object bInner = applicationContext.getBean("registerBeanClient_registerBeanBImpl_inner");
        Object bOuter = applicationContext.getBean("registerBeanClient_registerBeanBImpl_outer");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}