package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.Probe;
import com.tah.graduate_run.entity.U_Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ->  tah9  2021/10/15 17:53
 */
public interface RecordMapper {

    @Select("SELECT MAX(belonging)  FROM u_record WHERE uid=${uid}")
    Integer getBelonging(Integer uid);

    @Insert("insert into u_record (datetime,uid,pid,belonging)" +
            " values(#{datetime},#{uid},#{pid},#{belonging}) ")
    void insertRecord(Long datetime, Integer uid, Integer pid,Integer belonging);

    @Select("select * from probe where p_id=${pid}")
    Probe getProbe(Integer pid);

    @Select("select * from u_record where uid=${uid} and belonging=${belonging} ORDER BY DATETIME")
    List<U_Record> getRecord(Integer uid,Integer belonging);
}
