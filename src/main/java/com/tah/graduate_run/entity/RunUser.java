package com.tah.graduate_run.entity;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * ->  tah9  2021/9/9 10:16
 */
@Data
@ToString
public class RunUser {
    private String username;
    private Integer uid;
    private Integer belonging;
    public List<U_Record> u_records = new ArrayList<>();

    public RunUser(){}

    public RunUser(String username, Integer uid, Integer belonging) {
        this.username = username;
        this.uid = uid;
        this.belonging = belonging;
    }
}
