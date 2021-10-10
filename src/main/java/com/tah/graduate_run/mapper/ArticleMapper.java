package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * ->  tah9  2021/9/18 18:32
 */
public interface ArticleMapper {
    @Insert("insert into article(message_title,message,uid,pic,device_title,dateline,message_cover,feedType)" +
            " values(#{message_title},#{message},#{uid},#{pic},#{device_title},#{dateline},#{message_cover},#{feedType})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void createArticle(Article article);

    //查询动态
    @Select("SELECT a.*,b.`username`,b.`userAvatar`\n" +
            "FROM  `article` AS a\n" +
            "LEFT JOIN sys_user AS b \n" +
            "ON (b.`uid`=a.`uid`) \n" +
            " where feedType LIKE'%${feedType}%' "+
            " ${searchUid} "+
            " ${searchTag} "+
            "ORDER BY ${orderKey} DESC  " +
            "limit ${pagerNum},${pagerSize}")
    List<Article> getArticle(String searchTag,String searchUid,
                             String feedType,String orderKey,
                             Integer pagerNum, Integer pagerSize);

    //查询动态以及用户名称和头像=左查询两表联合
    @Select("SELECT a.*,b.`username`,b.`userAvatar` FROM article AS a " +
            " LEFT JOIN sys_user AS b " +
            " ON (b.`uid`=a.`uid`) "+
            " WHERE a.`id`=#{id}")
    Article getArticleInfo(String id);




}
