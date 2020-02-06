package com.sky.springcloud.client.controller;

import com.sky.springcloud.client.domain.CommonResult;
import com.sky.springcloud.client.domain.User;
import com.sky.springcloud.client.service.UserFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 使用feign方式调用远程服务。feign是对ribbon的封装。
 */
@RestController
@RequestMapping("/user/feign")
public class UserFeignController {
    @Autowired
    private UserFeignService userFeignService;

    @GetMapping("/getByUsername")
    public CommonResult getByUsername(@RequestParam String username) {
        return userFeignService.getByUsername(username);
    }

    @PostMapping("/create")
    public CommonResult create(@RequestBody User user) {
        return userFeignService.create(user);
    }
}
