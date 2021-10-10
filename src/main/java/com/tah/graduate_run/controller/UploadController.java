package com.tah.graduate_run.controller;

import com.tah.graduate_run.service.PicService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ->  tah9  2021/10/3 15:28
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Resource
    PicService picService;

    @Resource
    HttpServletRequest request;

    @PostMapping("/url")
    public String up(@RequestBody Map map){
        return picService.up(request,map.get("url").toString());
    }
}
