package com.tah.graduate_run.service;

import com.tah.graduate_run.config.MyToken;
import com.tah.graduate_run.entity.Tag;
import com.tah.graduate_run.mapper.TagMapper;
import com.tah.graduate_run.mapper.UserNumberMapper;
import com.tah.graduate_run.untils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * ->  tah9  2021/9/30 10:02
 */
@Service
public class TagService {
    @Resource
    TagMapper tagMapper;
    @Resource
    UserNumberMapper userNumberMapper;


    public Map getAllTags(Integer sort,Integer pagerNum,Integer pagerSize){
        if (sort==0){
            return Result.success().add("rows",tagMapper.getAllTags(pagerNum,pagerSize));
        }else {
            return Result.success().add("rows",tagMapper.getDescTags());
        }
    }


    public Map getHotWords(){
        LinkedHashSet<String> hotWords = tagMapper.getHotWords();
        String all = String.join(",", hotWords);
        String[] split = all.split(",");
        LinkedHashSet<String> result = new LinkedHashSet<>();
        result.addAll(Arrays.asList(split));
        return Result.success().add("rows",result);
    }
    public Map getMatchWords(Map map){
        List<Tag> matchTags = tagMapper.getMatchTags(map.get("word").toString());
        return Result.success().add("rows", matchTags);
    }
    private static final Logger log= LoggerFactory.getLogger(TagService.class);
    public Map getTagInfo(HttpServletRequest request,String title){
        String uid = MyToken.getUid(request);
        List<Integer> focusTag = userNumberMapper.getFocus(uid, "tag");
        Tag tag = tagMapper.getTagInfo(title);
        tag.setBeFollow(focusTag.contains(tag.getId()));
        String[] split = tag.getFollow_list().split(",");
        List<String> avatar = new ArrayList<>();
        int i = 0;
        for (String s : split) {
            if (i==3){
                break;
            }
            i++;
            avatar.add(tagMapper.getUserAvatar(s));
        }
        return Result.success().add("rows",tag ).add("users",avatar);
    }

}
