package com.sso.controller;

import com.sso.model.dtos.ResponseResult;
import com.sso.model.dtos.LoginDto;
import com.sso.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(value = "用户登录接口管理", description = "用户登录接口管理", tags = "user-login")
public class LoginController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginDto dto){
        return userService.login(dto);
    }

    @PostMapping("/upload")
    public String upload(@RequestBody LoginDto dto){
        return "haha";
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello啊";
    }


}
