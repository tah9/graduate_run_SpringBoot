package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

/**
 * ->  tah9  2021/10/15 17:52
 */
@Data
@ToString
public class U_Record {
    Integer id;
    Long datetime;
    Integer uid;
    Integer pid;
    Integer belonging;

    public U_Record(Long datetime, Integer uid, Integer pid, Integer belonging) {
        this.datetime = datetime;
        this.uid = uid;
        this.pid = pid;
        this.belonging = belonging;
    }
}
