package com.greenwich.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl {

    private final StringRedisTemplate redisTemplate;

    public void setValue(String key, String value, long duration, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, duration, unit);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

}
