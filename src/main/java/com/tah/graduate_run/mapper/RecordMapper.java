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


    @Select("select * from probe where p_id=${pid}")
    Probe getProbe(Integer pid);

}
