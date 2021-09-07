package com.tah.graduate_run.untils;

import java.util.HashMap;

/**
 * ->  tah9  2021/8/28 19:34
 */
public class MyMap extends HashMap {

    public MyMap add(Object key, Object value) {
        put(key, value);
        return this;
    }
}
