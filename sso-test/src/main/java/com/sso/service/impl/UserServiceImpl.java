package com.sso.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.mapper.UserMapper;
import com.sso.model.User;
import com.sso.model.dtos.LoginDto;
import com.sso.model.dtos.ResponseResult;
import com.sso.model.enums.AppHttpCodeEnum;
import com.sso.utils.RedisUtils;
import com.sso.service.UserService;
import com.sso.utils.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult login(LoginDto dto) {
        RedisTemplate redisTemplate = RedisUtils.redis;
        //1.处理普通用户登录
        if(StringUtils.isNotBlank(dto.getUsername()) && StringUtils.isNotBlank(dto.getPassword())){
            //1.1 根据用户名查询用户判断是否存在
            User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, dto.getUsername()));
            if(user==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
            }

            //1.2 比对登录密码与表中的密文密码
            String password = user.getPassword(); //用户在表中的密文密码
            String loginPwd = dto.getPassword(); //用户登录的明文密码
            boolean result = BCrypt.checkpw(loginPwd, password);
            //1.3 比较失败，响应密码错误
            if(!result){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }

            //1.4 比较成功，生成TOKEN
            String token = AppJwtUtil.getToken(user.getId().longValue());
            user.setPassword("");

            Map resultMap = new HashMap();
            resultMap.put("user", user);

            redisTemplate.opsForValue().set(user.getId().toString(),token,35 * 60 *1000, TimeUnit.SECONDS);

            return ResponseResult.okResult(resultMap);

        } else { //2.处理游客模式登录
            //直接为用户按照0生成TOKEN
            String token = AppJwtUtil.getToken(0L);
            Map resultMap = new HashMap();

            redisTemplate.opsForValue().set("0",token);

            return ResponseResult.okResult(resultMap);
        }
    }
}
