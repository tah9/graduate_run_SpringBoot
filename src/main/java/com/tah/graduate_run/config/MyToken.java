package com.tah.graduate_run.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tah.graduate_run.entity.SysUser;

/**
 * ->  tah9  2021/8/31 17:05
 */
public class MyToken {
    /* 使用手机号创建，用密码加密
     * @param: user
     * @return: java.lang.String
     */
   public static String create(SysUser user){
       String token = JWT.create().withAudience(String.valueOf(user.getPhone_number()))
               .sign(Algorithm.HMAC256(user.getPassword()));
       return token;
    }

    public static void verify(SysUser user,String token){
        if (user == null) {
            throw new RuntimeException("用户不存在，请重新登录");
        }
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("token和密码不匹配");
        }
    }
}
