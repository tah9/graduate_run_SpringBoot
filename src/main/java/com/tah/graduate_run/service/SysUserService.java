package com.tah.graduate_run.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tah.graduate_run.config.MyToken;
import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.mapper.SysUserMapper;
import com.tah.graduate_run.untils.Code;
import com.tah.graduate_run.untils.Other;
import com.tah.graduate_run.untils.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/8/28 10:15
 * 用户相关服务：登录、注册、修改密码
 */
@Service
public class SysUserService {
    @Resource
    SysUserMapper userMapper;


    /* 通过手机号登录，获取令牌并更新ip和时间
     * @param: phone_number
     * @param: password
     * @return: java.util.Map
     */
    public Map login(String phone_number, String password) {
        SysUser user = userMapper.getUserByPhone(phone_number);
        if (user == null || !new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return Result.fail(Code.ACC_ERR, "账号或密码错误");
        } else {
            user.setLogin_ip(Other.getIp());
            user.setLogin_time(Other.getTime("yyyy-MM-dd HH:mm:ss"));
            userMapper.upLogin(user);
            return Result.success().add("token", MyToken.create(user));
        }
    }
    /* 人脸登录
     * @param: map
     * @return: java.util.Map
     */
    public Map loginUseFace(Map map){
        try{
            SysUser user = userMapper.getUserByPhone(map.get("phone_number").toString());
            user.setLogin_ip(Other.getIp());
            user.setLogin_time(Other.getTime("yyyy-MM-dd HH:mm:ss"));
            userMapper.upLogin(user);
            MyToken.verify(user,map.get("token").toString());
            return Result.success().add("user",user);
        }catch (Exception e){
            return Result.fail(Code.ACC_ERR, e.toString());
        }
    }

    public Map changePWD(Map map) {
        try {
            SysUser user = userMapper.getUserById(map.get("user_id").toString());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            //判断请求中旧密码和用户旧密码是否相同
            if (!encoder.matches(map.get("password").toString(), user.getPassword())) {
                return Result.fail(Code.PWD_ERR, "修改失败,旧密码错误");
            }
            user.setPassword(encoder.encode(map.get("newpassword").toString()));
            userMapper.changePWD(user);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(Code.PWD_ERR, "修改失败");
        }
    }

    public Map addFace(String phone_number){
        try{
            userMapper.addFace(phone_number);
            return Result.success();
        }catch (Exception e){
            return Result.fail(Code.ADFACE_ERR, e.toString());
        }
    }

    public List<SysUser> getAllUser() {
        return userMapper.getAll();
    }

    /* 注册账号，参数：phone_number,password,username
     * @param: user
     * @return: java.util.Map
     */
    public Map register(SysUser user) {
        try {
            String password = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(password);
            String time = Other.getTime("yyyy-MM-dd HH:mm:ss");
            user.setCreate_time(time);
            user.setLogin_time(time);
            user.setLogin_ip(Other.getIp());
            user.setGender(user.getGender());
            userMapper.register(user);
            return Result.success().add("token",MyToken.create(user));
        } catch (DuplicateKeyException e) {
            return Result.fail(Code.REGISTER_ERR, "注册失败,手机号已存在");
        }
    }
}
