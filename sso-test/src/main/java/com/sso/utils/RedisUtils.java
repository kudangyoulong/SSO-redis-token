package com.sso.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate ;
    public static RedisTemplate redis;

    @PostConstruct
    public void getRedisTemplate() {
        redis = this.redisTemplate;
    }

}
