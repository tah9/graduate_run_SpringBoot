package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.SysUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ->  tah9  2021/8/27 16:43
 */
public interface SysUserMapper {

    @Select("select * from sys_user where phone_number =#{phone_number}")
    SysUser getUserByPhone(String phone_number);

    @Select("select * from sys_user where user_id =#{user_id}")
    SysUser getUserById(String user_id);

    @Update("update sys_user set password=#{password} where user_id = #{user_id}")
    void changePWD(SysUser user);

    @Update("update sys_user set face_counter=1 where phone_number = #{phone_number}")
    void addFace(String phone_number);

    @Select("Select * from sys_user")
    List<SysUser> getAll();

    @Insert("insert into sys_user(username,password,create_time,phone_number,login_ip,login_time)" +
            " values(#{username},#{password},#{create_time},#{phone_number},#{login_ip},#{login_time})")
    void register(SysUser user);

    @Update("update sys_user set login_ip=#{login_ip},login_time=#{login_time} where user_id = #{user_id}")
    void upLogin(SysUser user);
}
