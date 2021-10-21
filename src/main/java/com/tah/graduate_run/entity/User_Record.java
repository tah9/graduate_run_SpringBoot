package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

/**
 * ->  tah9  2021/10/18 10:54
 */
@Data
@ToString
public class User_Record {
    long id;
    long uid;
    long speed;
    long datetime;
    long time;
    long length;
    String record;
    String userAvatar;

    public User_Record(){

    }
    public User_Record(long uid, long time, String userAvatar) {
        this.uid = uid;
        this.time = time;
        this.userAvatar = userAvatar;
    }
}
