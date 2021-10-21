package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

/**
 * ->  tah9  2021/9/28 8:13
 */
@Data
@ToString
public class Comment {
    Integer id;
    Integer fid;
    Integer uid;
    Integer rid;
    Integer rrid;
    Integer replynum;
    String message;
    Integer likenum;
    Integer dateline;
    String pic;
    int status;
    String rusername;
    Integer feedUid;
    int isFeedAuthor;
    String username;
    String userAvatar;
    Integer rank_score;
    String recent_reply_ids;
    //是否已点赞
    boolean beLike;
}
