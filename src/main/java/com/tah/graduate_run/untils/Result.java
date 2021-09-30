package com.tah.graduate_run.untils;

import java.util.HashMap;
import java.util.Map;

public class Result {
    public static MyMap success() {
        MyMap map = new MyMap();
        map.put("code", 200);
        map.put("msg", "操作成功");
        return map;
    }
    public static MyMap fail(int code,String msg){
        MyMap map = new MyMap();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }
}

