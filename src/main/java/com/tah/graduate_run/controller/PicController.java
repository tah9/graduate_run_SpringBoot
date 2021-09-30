package com.tah.graduate_run.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.tah.graduate_run.service.PicService;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ->  tah9  2021/8/29 21:25
 */
@RestController
@RequestMapping("/face")
public class PicController {

    @Resource
    PicService picService;

    @Resource
    private RedisTemplate<String, Object> strRedisTemplate;

    @GetMapping("/test")
    public String test() {
        //strRedisTemplate.opsForValue().set("test", "zheshiceshi1");
        return (String) strRedisTemplate.opsForValue().get("test");
    }


    @PostMapping("/upload")
    @ResponseBody
    public Map upload(HttpServletRequest request) throws Exception {
        return picService.uptoUserFolder(request);
    }

}
