package com.tah.graduate_run.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ->  tah9  2021/9/25 13:14
 */
public interface UserNumberMapper {
    //查询关注列表
    @Select("SELECT follow_id FROM user_fans WHERE uid=#{uid}")
    List<Long> getFocus(String uid);
}
