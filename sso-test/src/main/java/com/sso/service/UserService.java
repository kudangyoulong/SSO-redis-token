package com.sso.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.model.User;
import com.sso.model.dtos.LoginDto;
import com.sso.model.dtos.ResponseResult;

public interface UserService extends IService<User> {

    public ResponseResult login(LoginDto dto);
}
