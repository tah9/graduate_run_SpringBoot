package com.tah.graduate_run.controller;

import com.tah.graduate_run.service.user.UserNumberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ->  tah9  2021/9/25 13:19
 */
@RestController
@RequestMapping("/user")
public class UserNumberController {

    @Resource
    UserNumberService service;

    //查看关注列表
    @GetMapping("/focus/{uid}")
    List<Long> userNumber(@PathVariable("uid") String uid){
        return service.getFocus(uid);
    }
}
