package com.tah.graduate_run.controller;

import com.tah.graduate_run.config.UseToken;
import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.entity.UserTemp;
import com.tah.graduate_run.service.SysUserService;
import com.tah.graduate_run.service.user.SysUserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * ->  tah9  2021/8/27 20:00
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Resource
    SysUserService userService; @Resource
    SysUserInfoService userInfoService;

    @PostMapping("/login")
    public Map login(@RequestBody SysUser user) {
        return userService.login(user.getPhone_number(), user.getPassword());
    }

    @UseToken
    @GetMapping("/useTokenGetUser")
    @ResponseBody
    public Map gerUserInfo(HttpServletRequest request){
        return userService.useTokenGetUser(request);
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


    @GetMapping("/all")
    public List<String> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/random10")
    public List<UserTemp> getAllRandomUser() {
        return userService.getRandom10();
    }


    @PostMapping("/register")
    public Map insert(@RequestBody SysUser user) {
        return userService.register(user);
    }

    @GetMapping("/likesCount/{id}")
    public int byThumbUpCount(@PathVariable("id") String  id){
        return userInfoService.byThumbUpCount(id);
    }

    @GetMapping("/info/{id}")
    public Map getUserInfo(@PathVariable("id") String id) {
        return userInfoService.getUserInfo(id);
    }
}
