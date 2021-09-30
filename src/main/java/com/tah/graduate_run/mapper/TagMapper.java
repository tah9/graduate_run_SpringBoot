package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.Tag;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ->  tah9  2021/9/29 22:52
 */
public interface TagMapper {
    @Select("SELECT * FROM article_tags ORDER BY hot_num DESC limit #{pagerNum},#{pagerSize}")
    List<Tag> getAllTags(Integer pagerNum,Integer pagerSize);
}
