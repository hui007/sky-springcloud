package com.sky.springcloud.client.service;

import com.sky.springcloud.client.domain.CommonResult;
import com.sky.springcloud.client.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 使用feign包装远程服务
 */
@FeignClient(value = "${service-in-eureka.sky-server-ribbon}")
//@FeignClient(value = "${service-in-eureka.sky-server-feign}")
//@FeignClient(value = "sky-server")
public interface UserFeignService {

    @PostMapping("/user/create")
    public CommonResult create(@RequestBody User user);

    @GetMapping("/user/getByUsername")
    public CommonResult getByUsername(@RequestParam String username);
}
