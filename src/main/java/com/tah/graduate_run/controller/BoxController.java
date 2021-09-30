package com.tah.graduate_run.controller;

import com.tah.graduate_run.service.BoxService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ->  tah9  2021/9/20 18:13
 */
@RestController
@RequestMapping("/box")
public class BoxController {
    @Resource
    BoxService service;

    @PostMapping("/input")
    @ResponseBody
    public void inputFace(HttpServletRequest request)throws Exception{
        service.inputFace(request);
    }

    @DeleteMapping("/delete/{name}")
    public void deleteFace(@PathVariable("name")String name){
        service.deleteFace(name);
    }

    @GetMapping("/get")
    public List<String> getFace(){
        return service.getFace();
    }
}
