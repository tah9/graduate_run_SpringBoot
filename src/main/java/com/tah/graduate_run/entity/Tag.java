package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

/**
 * ->  tah9  2021/9/29 22:50
 */
@Data
@ToString
public class Tag {
    Integer id;
    String title;
    String logo;
    String cover;
    String description;
    Integer follownum;
    Integer hot_num;
    Integer dateline;
    Integer lastupdate;
    Integer feednum;
    String intro;
    String follow_list;
    String keywords;

    //是否关注
    boolean beFollow;
}
