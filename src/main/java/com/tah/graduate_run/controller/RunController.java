package com.tah.graduate_run.controller;

import com.tah.graduate_run.service.EnterExit;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * ->  tah9  2021/9/9 10:37
 */
@RestController
@RequestMapping("/run")
public class RunController {

    @Resource
    EnterExit enterExit;

    @PostMapping("/enter")
    public Map enterUser(String userName){
        return enterExit.enterUser(userName);
    }

    @PostMapping("/exit")
    public Map exitUser(String userName){
        return enterExit.exitUser(userName);
    }

}
