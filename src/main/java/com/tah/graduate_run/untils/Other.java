package com.tah.graduate_run.untils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * ->  tah9  2021/8/28 21:43
 */
public class Other {
    public static String getRandomNumber(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }
    public static String getTime(String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(pattern);
        Date date = new Date();
        return sdf.format(date);
    }
    public static String getIp(){
        String hostAddress="";
        try {
             hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println(hostAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostAddress;
    }
}
