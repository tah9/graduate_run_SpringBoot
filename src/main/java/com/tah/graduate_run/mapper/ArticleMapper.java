package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.Article;
import com.tah.graduate_run.entity.UpArticle;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/18 18:32
 */
public interface ArticleMapper {
    @Insert("insert into article(message_title,message,uid,pic,device_title,dateline,message_cover)" +
            " values(#{message_title},#{message},#{uid},#{pic},#{device_title},#{dateline},#{message_cover})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void createArticle(UpArticle article);

    //查询动态以及用户名称和头像=左查询两表联合
    @Select("SELECT a.*,b.`username`,b.`userAvatar`\n" +
            "FROM  `article` AS a\n" +
            "LEFT JOIN sys_user AS b \n" +
            "ON (b.`uid`=a.`uid`) \n" +
            "ORDER BY dateline DESC  " +
            "limit #{pagerNum},#{pagerSize}")
    List<Article> getArticle(Integer pagerNum, Integer pagerSize);

    //查询用户所有动态
    @Select("SELECT a.*,b.`username`,b.`userAvatar`\n" +
            "FROM  `article` AS a\n" +
            "LEFT JOIN sys_user AS b \n" +
            "ON (b.`uid`=a.`uid`) \n" +
            "WHERE a.`uid`=#{uid}\n" +
            "ORDER BY likenum")
    List<Article> getUserAllArticle(String uid);

    //查询动态以及用户名称和头像=左查询两表联合
    @Select("SELECT a.*,b.`username`,b.`userAvatar` FROM article AS a " +
            " LEFT JOIN sys_user AS b " +
            " ON (b.`uid`=a.`uid`) "+
            " WHERE a.`id`=#{id}")
    Article getArticleInfo(String id);

    @Update("update article set likenum=likenum-1 where id=#{id}")
    void likeNumDown(@Param("params") Map map);

    @Update("update article set likenum=likenum+1 where id=#{id}")
    void likeNumUp(@Param("params") Map map);

    @Insert("insert into article_like(uid,id,aid)" +
            " values(#{uid},#{id},#{aid})")
    void insertLike(@Param("params") Map map);

    @Delete("delete from article_like where id=#{id} and uid=#{uid}")
    void deleteLike(@Param("params") Map map);

    @Select("select id from article_like where uid=#{id}")
    List<Long> myLikes(String id);



}
