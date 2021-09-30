package com.tah.graduate_run.service;

import com.tah.graduate_run.entity.Tag;
import com.tah.graduate_run.mapper.TagMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ->  tah9  2021/9/30 10:02
 */
@Service
public class TagService {
    @Resource
    TagMapper tagMapper;

    public List<Tag> getAllTags(Integer pagerNum,Integer pagerSize){
        return tagMapper.getAllTags(pagerNum,pagerSize);
    }
}
