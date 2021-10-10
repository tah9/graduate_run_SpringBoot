package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

/**
 * ->  tah9  2021/9/28 8:13
 */
@Data
@ToString
public class Comment {
    long id;
    long fid;
    long uid;
    long rid;
    long rrid;
    long replynum;
    String message;
    long likenum;
    long dateline;
    String pic;
    int status;
    String rusername;
    long feedUid;
    int isFeedAuthor;
    String username;
    String userAvatar;
    long rank_score;
    String recent_reply_ids;
    //是否已点赞
    boolean beLike;
}
