package com.tah.graduate_run.controller;

import com.tah.graduate_run.service.comment.CommentLoadService;
import org.springframework.web.bind.annotation.*;

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
    public Map getCommentList(HttpServletRequest request,@PathVariable("fid")String fid){
        return loadService.getCommentList(request,fid);
    }

    @PostMapping("/commentPush")
    public Map pushComment(HttpServletRequest request){
        return loadService.pushComment(request);
    }
}
