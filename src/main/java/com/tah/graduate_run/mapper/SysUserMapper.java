package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.entity.UserTemp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ->  tah9  2021/8/27 16:43
 */
public interface SysUserMapper {

    @Select("SELECT  b.`username`,b.`phone_number`\n" +
            "FROM  sys_user AS b \n" +
            "ORDER BY RAND()\n" +
            "LIMIT 10;")
    List<UserTemp> getRandom10();

    @Update("update sys_user set username='${username}',userAvatar='${userAvatar}'," +
            "cover='${cover}',bio='${bio}',gender=${gender},height=${height}," +
            "weight=${weight},birthday='${birthday}' where uid=${uid}")
    void changeUser(SysUser user);

    @Select("select * from sys_user where phone_number =#{phone_number}")
    SysUser getUserByPhone(String phone_number);

    @Select("select * from sys_user where uid =#{uid}")
    SysUser getUserById(String uid);

    @Update("update sys_user set password=#{password} where uid = #{uid}")
    void changePWD(SysUser user);


    @Select("Select username from sys_user")
    List<String> getAll();



    @Insert("insert into sys_user(username)" +
            " values(#{username})")
    void insertTourist(String username);

    @Insert("insert into sys_user(username,password,create_time,phone_number,login_ip,logintime)" +
            " values(#{username},#{password},#{create_time},#{phone_number},#{login_ip},#{logintime})")
    void register(SysUser user);

    //更新登录地，登录时间
    @Update("update sys_user set login_ip=#{login_ip},logintime=#{logintime} where uid = #{uid}")
    void upLogin(SysUser user);

//    //查询用户被点赞数,aid=被点赞用户id
//    @Select("SELECT COUNT(aid) FROM article_like WHERE aid=#{aid}")
//    int byThumbUpCount(String aid);

//    //查询粉丝数
//    @Select("SELECT COUNT(uid),COUNT(follow_id) FROM user_fans WHERE uid=#{uid}")
//    int[] test(String uid);



    //查看用户信息
    @Select("SELECT a.`uid`,a.`username`,a.`login_ip`,a.`logintime`,a.`userAvatar`,a.`cover`" +
            ",a.`gender`,a.`bio`,a.`follow`,a.`fans`,a.`be_like_num` FROM sys_user AS a \n" +
            "WHERE a.`username`=#{username}")
    SysUser getUserInfo(String username);

//    @Select("SELECT COUNT(a.`uid`) AS 关注 " +
//            " FROM user_fans AS a " +
//            " WHERE a.`uid`=#{id}")
//    int getFocusOnSum(String id);
////
//    @Select("SELECT COUNT(a.`follow_id`) AS 粉丝 " +
//            " FROM user_fans AS a " +
//            " WHERE a.`follow_id`=#{id}")
//    int getFansSum(String id);
//
//    @Select("SELECT COUNT(b.`aid`)  AS 获赞 " +
//            " FROM article_like AS b " +
//            " WHERE b.`aid`=#{id}")
//    int getLikesSum(String id);

}
