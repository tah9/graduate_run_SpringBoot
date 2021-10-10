package com.tah.graduate_run.entity;

import lombok.Data;

/**
 * ->  tah9  2021/8/27 16:41
 */
@Data
public class SysUser {
    private long uid;
    private String username;
    private String password;
    private String create_time;
    private String phone_number;
    private String login_ip;
    private String logintime;
    private String userAvatar;
    private int height;
    private int weight;
    private long gender;
    private String birthday;
    private String cover;
    String bio;
    //关注数，粉丝数，被点赞数
    long follow;
    long fans;
    long be_like_num;

    //是否记录人脸
    boolean hasFace;
    //是否已关注
    boolean beFollow;
}
