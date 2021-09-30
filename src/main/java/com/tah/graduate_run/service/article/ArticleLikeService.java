package com.tah.graduate_run.service.article;

import com.tah.graduate_run.mapper.ArticleMapper;
import com.tah.graduate_run.untils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/20 14:32
 */
@Service
public class ArticleLikeService {
    @Resource
    ArticleMapper mapper;

    public Map myLikes(String id){
        return Result.success().add("likes", mapper.myLikes(id));
    }
}
