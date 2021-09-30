package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ->  tah9  2021/9/28 8:17
 */

public interface CommentMapper {
    //查询动态下所有评论
    @Select("SELECT a.*,b.`username`,b.`userAvatar` FROM article_comment AS a " +
            "             LEFT JOIN sys_user AS b " +
            "             ON (b.`uid`=a.`uid`) " +
            "             WHERE a.`fid`=#{fid}")
    List<Comment> getArticleComment(String fid);
}
