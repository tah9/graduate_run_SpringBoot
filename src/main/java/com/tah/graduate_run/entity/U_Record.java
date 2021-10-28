package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

/**
 * ->  tah9  2021/10/15 17:52
 */

@Data
@ToString
public class U_Record {
    Long datetime;
    Integer pid;

    public U_Record(Long datetime, Integer pid) {
        this.datetime = datetime;
        this.pid = pid;
    }
}
