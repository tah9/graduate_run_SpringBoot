package com.tah.graduate_run.config;

/**
 * ->  tah9  2021/8/28 14:58
 */

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.mapper.SysUserMapper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    SysUserMapper mapper;

    /* 拦截需要token令牌的请求
     * @param: httpServletRequest
     * @param: httpServletResponse
     * @param: object
     * @return: boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查需要用户权限的注解
        if (method.isAnnotationPresent(UseToken.class)) {
            UseToken useToken = method.getAnnotation(UseToken.class);
            if (useToken.required()) {
                String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                String phone_number;
                try {
                    phone_number = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("手机号不匹配");
                }
                SysUser user = mapper.getUserByPhone(phone_number);
                MyToken.verify(user,token);
                return true;
            }
        }
        return true;
    }
}

