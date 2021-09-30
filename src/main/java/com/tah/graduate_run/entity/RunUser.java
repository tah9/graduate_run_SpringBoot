package com.tah.graduate_run.entity;

import lombok.Data;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * ->  tah9  2021/9/9 10:16
 */
@Data
public class RunUser {
    private String username;
    private Queue<String> time = new ArrayDeque<>();

    public RunUser(String username) {
        this.username = username;
    }
}
