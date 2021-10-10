package com.tah.graduate_run.controller;

import com.tah.graduate_run.config.UseToken;
import com.tah.graduate_run.entity.Article;
import com.tah.graduate_run.service.article.ArticleLikeService;
import com.tah.graduate_run.service.article.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/18 18:45
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    HttpServletRequest request;

    @Resource
    ArticleService service;
    @Resource
    ArticleLikeService likeService;


    @PostMapping("/create")
    @ResponseBody
    public Map createArticle(HttpServletRequest request) {
        return service.createArticle(request);
    }

    @PostMapping("/upPics")
    @ResponseBody
    public String upPics(HttpServletRequest request) throws Exception {
        return service.upPics(request);
    }
    @UseToken
    @GetMapping("/get")
    public Map getArticle(HttpServletRequest request,
                          @RequestParam(defaultValue = "") String tags,
                          @RequestParam(defaultValue = "") String uid,
                          @RequestParam(defaultValue = "") String feedType,
                          @RequestParam(defaultValue = "dateline") String orderBy,
                          @RequestParam(defaultValue = "1") Integer pagerNum,
                          @RequestParam(defaultValue = "10") Integer pagerSize) {
        return service.getArticle(request, tags,uid,feedType,orderBy, (pagerNum - 1) * pagerSize, pagerSize);
    }

    @UseToken
    @GetMapping("/info/{id}")
    public Map getInfo(@PathVariable("id") String id) {
        return service.getArticleInfo(request, id);
    }

    @PostMapping("/like")
    public Map likeNumUp(@RequestBody Map map) {
        return service.likeNumUp(map);
    }

    @PostMapping("/dislike")
    public Map likeNumDown(@RequestBody Map map) {
        return service.likeNumDown(map);
    }

    @GetMapping("/likes/{id}")
    public Map myLikes(@PathVariable("id") String id) {
        return likeService.myLikes(id);
    }


}
