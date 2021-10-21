package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.SysUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/25 13:14
 */
public interface UserNumberMapper {
    //查询关注的id
    @Select("SELECT follow_id FROM user_fans WHERE uid=#{uid} and type like'${type}'")
    List<Integer> getFocus(String uid,String type);


    //查询关注的用户
    @Select("SELECT a.`follow_id`,b.`username`,b.`userAvatar` FROM user_fans AS a " +
            "LEFT JOIN sys_user AS b " +
            "ON (b.uid=a.`follow_id`) " +
            " WHERE a.`uid`=#{uid} and a.`type` like 'user'" +
            " limit ${pagerNum},${pagerSize}")
    List<SysUser> getFollowUser(String uid, int pagerNum, int pagerSize);

    @Update("update article set likenum=likenum-1 where id=#{id}")
    void likeNumDown(@Param("params") Map map);

    @Update("update article_comment set likenum=likenum-1 where id=#{id}")
    void commentLikeNumDown(@Param("params") Map map);

    @Update("update article set likenum=likenum+1 where id=#{id}")
    void likeNumUp(@Param("params") Map map);

    @Update("update article_comment set likenum=likenum+1 where id=#{id}")
    void commentLikeNum(@Param("params") Map map);


    @Insert("insert into article_like(uid,id,ruid,type)" +
            " values(#{uid},#{id},#{ruid},#{type})")
    void insertLike(@Param("params") Map map);

    @Delete("delete from article_like where id=${id} and uid=${uid} and type like '${type}'")
    void deleteLike(@Param("params") Map map);

    @Select("select id from article_like where uid=${uid} and type like '${type}'")
    List<Integer> myLikes(String type, String uid);

    @Insert("insert into user_fans(uid,follow_id,type) values(#{uid},#{follow_id},#{type})")
    void toFocus(@Param("params") Map map);

    @Delete("delete from user_fans WHERE uid=${uid} AND follow_id=${follow_id} AND type LIKE'${type}'")
    void unFoucs(@Param("params") Map map);

    @Select("SELECT COUNT(id) FROM article WHERE uid=${uid}")
    int articleSum(String uid);
}
