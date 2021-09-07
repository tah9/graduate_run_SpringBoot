package com.tah.graduate_run.controller;

import com.tah.graduate_run.service.PicService;
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


    @GetMapping("/test")
    public String test() {
        return "test";
    }


    @PostMapping("/upload")
    @ResponseBody
    public Map upload(HttpServletRequest request) throws Exception {
        return picService.uptoUserFolder(request);
    }

}
