package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * ->  tah9  2021/9/26 23:03
 */
@Data
@ToString
public class UpArticle {
    private long id;
    private String message_title;
    private String message;
    private String message_cover;
    private long uid;
//    private int likenum;
//    private int commentnum;
//    private int favnum;
    private int status;
    private long dateline;
//    private String isheadline;
    private String tags;
    private String device_title;
    private String feedType;
    private String pic;
//    private String share_num;
    private boolean isLike;
    private boolean isFocus;
}
