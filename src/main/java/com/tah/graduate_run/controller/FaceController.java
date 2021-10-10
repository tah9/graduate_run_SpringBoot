package com.tah.graduate_run.controller;

import com.tah.graduate_run.config.UseToken;
import com.tah.graduate_run.service.user.FaceUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ->  tah9  2021/10/6 8:31
 */
@RestController
@RequestMapping("/face")
public class FaceController {
    @Resource
    FaceUserService faceUserService;

    @Resource
    HttpServletRequest request;

    @GetMapping("/allUser")
    public Map getAllUser(){
        return faceUserService.getAllUser();
    }


    @UseToken
    @PostMapping("/addface/{username}")
    public Map addFace(HttpServletRequest request,@PathVariable("username")String username){
        return faceUserService.addFace(request,username);
    }
}
