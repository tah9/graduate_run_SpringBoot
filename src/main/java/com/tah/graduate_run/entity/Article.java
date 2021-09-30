package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * ->  tah9  2021/9/18 18:36
 */
@Data
@ToString
public class Article {
    private long id;
    private String message_title;
    private String message;
    private String message_cover;
    private long uid;
    private int likenum;
    private int commentnum;
    private int favnum;
    private int status;
    private long dateline;
    private String isheadline;
    private String tags;
    private String device_title;
    private String feedType;
    private String pic;
    private String username;
    private String userAvatar;
    private String share_num;
    public List<String> picArr;
    private boolean isLike;
    private boolean isFocus;
    long rank_score;
    String recent_reply_ids;
    String recent_hot_reply_ids;
    String replynum;
}
