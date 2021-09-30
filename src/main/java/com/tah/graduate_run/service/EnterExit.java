package com.tah.graduate_run.service;

import com.tah.graduate_run.untils.Result;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * ->  tah9  2021/9/9 10:11
 */
@Service
public class EnterExit {
    @Resource
    RedisTemplate<String,Object> redis;


    public Map enterUser(String userName){
        ListOperations<String, Object> list = redis.opsForList();
        list.rightPush("runUser", userName);
        return Result.success();
    }

    public Map exitUser(String userName){
//        Iterator<String> iterator = usernameQueue.iterator();
//        String name = null;
//        while (iterator.hasNext()) {
//            name = iterator.next();
//            if (name.equals(userName)) {
//                break;
//            }
//        }
        return Result.success();
    }
}
