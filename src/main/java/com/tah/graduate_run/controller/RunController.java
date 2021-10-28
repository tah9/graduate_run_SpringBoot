package com.tah.graduate_run.controller;

import com.tah.graduate_run.entity.RunUser;
import com.tah.graduate_run.service.EnterExitSerive;
import com.tah.graduate_run.untils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/9 10:37
 */
@RestController
@RequestMapping("/run")
public class RunController {

    @Resource
    EnterExitSerive enterExitSerive;

    @PostMapping("/enter")
    public Map enterUser(@RequestBody Map map){
        return enterExitSerive.enterUser(map);
    }

    @PostMapping("/exit")
    public Map exitUser(@RequestBody Map map){
        return enterExitSerive.exitUser(map);
    }

    @GetMapping("/getUser")
    public Map getBeingUser() {
        List<String> namelist = new ArrayList<>();
        for (RunUser runUser : EnterExitSerive.beUser) {
            namelist.add(runUser.getUsername());
        }
        return Result.success(namelist);
    }

}
