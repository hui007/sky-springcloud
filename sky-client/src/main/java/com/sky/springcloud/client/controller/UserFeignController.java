package com.sky.springcloud.client.controller;

import com.sky.springcloud.client.domain.CommonResult;
import com.sky.springcloud.client.domain.User;
import com.sky.springcloud.client.service.UserFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 使用feign方式调用远程服务。feign是对ribbon的封装。
 */
@Api(value = "使用feign方式调用远程服务")
@RestController
@RequestMapping("/user/feign")
public class UserFeignController {
    @Autowired(required = false)
//    @Autowired
    private UserFeignService userFeignService;

    @ApiOperation(value = "根据用户名获取用户", notes="用户名必填")
    @ApiImplicitParam(name = "username", required = true)
    @GetMapping("/getByUsername")
    public CommonResult getByUsername(@RequestParam String username) {
        return userFeignService.getByUsername(username);
    }

    @ApiOperation(value = "新建用户", notes="post请求中请求参数是一个json,包括用户名密码,例如:{\"username\":\"joshui\",\"password\":\"123456\"}")
    @ApiImplicitParam(name = "user", required = true)
    @PostMapping("/create")
    public CommonResult create(@RequestBody User user) {
        return userFeignService.create(user);
    }
}
