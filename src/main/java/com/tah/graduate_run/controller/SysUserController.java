package com.tah.graduate_run.controller;

import com.tah.graduate_run.config.UseToken;
import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * ->  tah9  2021/8/27 20:00
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Resource
    SysUserService userService;

    @PostMapping("/login")
    public Map login(@RequestBody SysUser user) {
        return userService.login(user.getPhone_number(), user.getPassword());
    }

    @UseToken
    @PostMapping("/loginface")
    public Map loginFace(@RequestBody Map map){
        return userService.loginUseFace(map);
    }

    @UseToken
    @PutMapping("/changepwd")
    public Map changepwd(@RequestBody Map map) {
        return userService.changePWD(map);
    }

    @UseToken
    @PutMapping("/addface/{phone_number}")
    public Map addFace(@PathVariable("phone_number")String phone_number){
        return userService.addFace(phone_number);
    }

    @UseToken
    @GetMapping("/all")
    public List<SysUser> getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping("/register")
    public Map insert(@RequestBody SysUser user) {
        return userService.register(user);
    }
}
