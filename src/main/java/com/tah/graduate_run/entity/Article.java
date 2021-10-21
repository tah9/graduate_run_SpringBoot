package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/18 18:36
 */
@Data
@ToString
public class Article {
    private Integer id;
    private String message_title;
    private String message;
    private String message_cover;
    private Integer uid;
    private int likenum;
    private int commentnum;
    private int favnum;
    private int status;
    private Integer dateline;
    private String isheadline;
    private String tags;
    private String device_title;
    private String feedType;
    private String pic;
    private String share_num;
    Integer rank_score;
    String recent_reply_ids;
    String recent_hot_reply_ids;
    String replynum;


    //用户信息
    private String username;
    private String userAvatar;

    //最热门评论
    Comment firstComment;
    //是否已关注
    boolean beFollow;
    //是否已点赞
    boolean beLike;
}
