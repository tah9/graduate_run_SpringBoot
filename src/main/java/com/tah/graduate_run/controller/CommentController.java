package com.tah.graduate_run.controller;

import com.tah.graduate_run.service.comment.CommentLoadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ->  tah9  2021/9/28 8:23
 */
@RestController
@RequestMapping("/article")
public class CommentController {
    @Resource
    CommentLoadService loadService;

    @Resource
    HttpServletRequest request;

    @GetMapping("/comment/{fid}")
    public Map getCommentList(@PathVariable("fid")String fid){
        return loadService.getCommentList(fid);
    }
}
