package com.tah.graduate_run.controller;

import com.tah.graduate_run.entity.Tag;
import com.tah.graduate_run.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/30 10:03
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Resource
    TagService tagService;

    @Resource
    HttpServletRequest request;

    @GetMapping("/all")
    public Map getAllTag(@RequestParam(defaultValue = "0") Integer sort,
                         @RequestParam(defaultValue = "1") Integer pagerNum,
                         @RequestParam(defaultValue = "10") Integer pagerSize) {

        return tagService.getAllTags(sort, (pagerNum - 1) * pagerSize, pagerSize);
    }


    @GetMapping("/hotWords")
    public Map getHotWords() {
        return tagService.getHotWords();
    }

    @PostMapping("/match")
    public Map getMatchTags(@RequestBody Map map) {
        return tagService.getMatchWords(map);
    }

    @PostMapping("/info")
    public Map getInfo(HttpServletRequest request,@RequestBody Map map) {
        return tagService.getTagInfo(request,map.get("title").toString());
    }
}
