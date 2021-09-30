package com.tah.graduate_run.controller;

import com.tah.graduate_run.entity.Tag;
import com.tah.graduate_run.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ->  tah9  2021/9/30 10:03
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Resource
    TagService tagService;
    @GetMapping("/all")
    public List<Tag> getAllTag(@RequestParam(defaultValue = "1") Integer pagerNum,
                               @RequestParam(defaultValue = "10") Integer pagerSize){
        return tagService.getAllTags((pagerNum - 1) * pagerSize,pagerSize);
    }
}
