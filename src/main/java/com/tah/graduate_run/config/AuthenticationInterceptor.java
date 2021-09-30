package com.tah.graduate_run.config;

/**
 * ->  tah9  2021/8/28 14:58
 */

import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.mapper.SysUserMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {


    /* 拦截需要token令牌的请求（使用UseToken注解）
     * @param: httpServletRequest
     * @param: httpServletResponse
     * @param: object
     * @return: boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查注解是否存在
        if (!method.isAnnotationPresent(UseToken.class)) {
            return true;
        }
        UseToken useToken = method.getAnnotation(UseToken.class);
        //检查注解是否开启
        if (!useToken.required()) {
            return true;
        }
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 执行认证
        if (token == null) {
            throw new RuntimeException("无token，请重新登录");
        }
        try {
            MyToken.verify(token);
        }catch (SignatureException | MalformedJwtException e){
            throw new RuntimeException("token验证失败");
        }catch (ExpiredJwtException e){
            throw new RuntimeException("token已过期");
        }
        return true;
    }
}

