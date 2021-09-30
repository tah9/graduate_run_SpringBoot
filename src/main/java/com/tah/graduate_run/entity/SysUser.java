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
    private String nickname;
    private String create_time;
    private String phone_number;
    private String login_ip;
    private String logintime;
    private String userAvatar;
    private int height;
    private int weight;
    private String gender;
    private String birthDay;
    private String cover;
}
