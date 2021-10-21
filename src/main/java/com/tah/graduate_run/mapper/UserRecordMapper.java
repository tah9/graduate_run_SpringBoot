package com.tah.graduate_run.mapper;

import com.tah.graduate_run.entity.User_Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ->  tah9  2021/10/18 11:00
 */
public interface UserRecordMapper {
    @Select("SELECT a.*,b.`userAvatar`\n " +
            " FROM  `user_record` AS a " +
            " LEFT JOIN sys_user AS b " +
            " ON (b.`uid`=a.`uid`) " +
            " ORDER BY a.speed ")
    List<User_Record> getMaxSpeed();

    @Select("SELECT a.*,b.`userAvatar` " +
            " FROM  `user_record` AS a " +
            " LEFT JOIN sys_user AS b " +
            " ON (b.`uid`=a.`uid`) ")
    List<User_Record> getAllRecord();

    @Insert("insert into user_record(uid,speed,datetime,time,record,length)" +
            " values(${uid},${speed},${datetime},${time},'${record}',${length})")
    void insertUserRecord(Integer uid, long speed, long datetime, long time, String record, long length);

    @Select("SELECT a.*,b.`userAvatar` " +
            " FROM  `user_record` AS a " +
            " LEFT JOIN sys_user AS b " +
            " ON (b.`uid`=a.`uid`) " +
            " where a.`uid`=${uid} and a.`datetime`>${startTime}")
    List<User_Record> getUserRecord(long uid,long startTime);
}
