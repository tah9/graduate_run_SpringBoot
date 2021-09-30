package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

/**
 * ->  tah9  2021/9/29 22:50
 */
@Data
@ToString
public class Tag {
    long id;
    String title;
    String logo;
    String cover;
    String description;
    long follownum;
    long hot_num;
    long dateline;
    long lastupdate;
    long feednum;
    String intro;
    String follow_list;
}
