package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Select("SELECT a.*,b.username FROM article_comment AS a \n" +
            "LEFT JOIN  sys_user AS b \n" +
            "ON (a.uid=b.uid) \n" +
            "WHERE a.`id`=#{id}")
    Comment getFirstComment(String id);

    @Insert("insert into article_comment(fid,uid,rid,rrid,message,dateline,pic,rusername,isFeedAuthor)" +
            " values(#{fid},#{uid},#{rid},#{rrid},#{message},#{dateline},#{pic},#{rusername},#{isFeedAuthor})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertComment(Comment comment);

    @Update("update article set replynum=replynum+1 where id=${id}")
    void updateArticleComment(Integer id);

    @Update("update article_comment set replynum=replynum+1 where id=${rrid}")
    void updateCommentReplyNum(Integer rrid);

    @Select("select recent_reply_ids from article_comment where id=${id}")
    String queryReplyIds(Integer id);

    @Update("update article_comment set recent_reply_ids='${newids}' where id=${rrid}")
    void updateReplyIds(String newids, Integer rrid);
}
