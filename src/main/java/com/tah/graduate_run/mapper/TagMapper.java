package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.Tag;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * ->  tah9  2021/9/29 22:52
 */
public interface TagMapper {
    @Select("SELECT * FROM article_tags ORDER BY hot_num DESC limit ${pagerNum},${pagerSize}")
    List<Tag> getAllTags(Integer pagerNum,Integer pagerSize);

    @Select("SELECT * FROM article_tags ORDER BY title DESC limit 1,10")
    List<Tag> getDescTags();


    @Select("SELECT keywords FROM article_tags\n" +
            "WHERE keywords NOT LIKE''\n" +
            "ORDER BY hot_num DESC")
    LinkedHashSet<String> getHotWords();

    @Select("SELECT * FROM article_tags  " +
            " WHERE keywords LIKE'%${words}%' " +
            " ORDER BY feednum DESC")
    List<Tag> getMatchTags(String words);

    @Select("SELECT * FROM article_tags " +
            " where title LIKE'${title}'")
    Tag getTagInfo(String title);

    @Select("select userAvatar from sys_user where uid=${uid}")
    String getUserAvatar(String uid);
}
