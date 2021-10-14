package com.tah.graduate_run.config;

import com.tah.graduate_run.untils.RedisObjectSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * ->  tah9  2021/9/12 18:11
 */
//@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
    {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisObjectSerializer redisObjectSerializer = new RedisObjectSerializer();

        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(redisObjectSerializer);
        return template;
    }
}
