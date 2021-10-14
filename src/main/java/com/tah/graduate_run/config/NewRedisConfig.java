package com.tah.graduate_run.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;

/**
 *
 **/
@Configuration
@ConditionalOnClass(RedisOperations.class)
//@EnableConfigurationProperties(RedisProperties.class)
//@Import({LettuceClientConfiguration.class, JedisClientConfiguration.class})
public class NewRedisConfig {


//    @Bean
//    @Primary
//    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate redisTemplate = new RedisTemplate();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//        //重点在这四行代码
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

    @Primary
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        //重点在这四行代码
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;

    }


//    @Bean(name = "stringRedisTemplate")
//
//    @ConditionalOnMissingBean
//
//    public StringRedisTemplate stringRedisTemplate(
//
//            RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//
//        StringRedisTemplate template = new StringRedisTemplate();
//
//        template.setConnectionFactory(redisConnectionFactory);
//
//        return template;
//
//    }
}