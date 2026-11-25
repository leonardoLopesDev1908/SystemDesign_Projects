package com.tickets.Utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

@Service
public class RedisDistributedLock {

    private final StringRedisTemplate redisTemplate;

    private static final String LUA_SCRIPT =
        """
        if redis.call("GET", KEYS[1]) == ARGV[1] then   
            return redis.call("DEL", KEYS[1])
        else
            return 0
        end
        """;

    public RedisDistributedLock(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public String acquiredLock(String lockKey, Duration expireTime) {
        String uniqueToken = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, uniqueToken, expireTime);

        return Boolean.TRUE.equals(success) ? uniqueToken : null;
    }

    public boolean releaseLock(String lockKey, String uniqueToken) {
        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(LUA_SCRIPT, Long.class),
                Collections.singletonList(lockKey),
                uniqueToken
        );

        return result != null &&     result > 0;
    }
}
