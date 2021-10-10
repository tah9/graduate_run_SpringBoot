package com.tah.graduate_run.service;

import com.tah.graduate_run.config.MyToken;
import com.tah.graduate_run.entity.Article;
import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.entity.UserTemp;
import com.tah.graduate_run.mapper.SysUserMapper;
import com.tah.graduate_run.service.article.ArticleService;
import com.tah.graduate_run.untils.Code;
import com.tah.graduate_run.untils.MyMap;
import com.tah.graduate_run.untils.Other;
import com.tah.graduate_run.untils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
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

    @Value("${face-file-path}")
    private String faceFilePath;

    public Map useTokenGetUser(HttpServletRequest request) {
        try {
            String uid = MyToken.getUid(request);
            SysUser user = userMapper.getUserById(uid);
            user.setPassword(null);
            //检查是否存在人脸
            List<String> list = new ArrayList<>();
            File file = new File(faceFilePath);
            for (File listFile : file.listFiles()) {
                System.out.println(listFile.getName());
                list.add(listFile.getName());
            }
            user.setHasFace(list.contains(user.getUsername()));
            return Result.success().add("userInfo", user);
        } catch (Exception e) {
            return Result.fail(404, e.toString());
        }
    }



    /* 通过手机号登录，获取令牌并更新ip和时间
     * @param: phone_number
     * @param: password
     * @return: java.util.Map
     */
    private static final Logger log= LoggerFactory.getLogger(SysUserService.class);
    public Map login(String phone_number, String password) {
        SysUser user = userMapper.getUserByPhone(phone_number);
        log.info(user.toString());
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return Result.fail(Code.ACC_ERR, "账号或密码错误");
        } else {
            user.setLogin_ip(Other.getIp());
            user.setLogintime(System.currentTimeMillis() / 1000 + "");
            userMapper.upLogin(user);
            MyMap map = new MyMap<>();
            map.add("uid", user.getUid());
            return Result.success().add("token", MyToken.create(map));
        }
    }

    /* 人脸登录
     * @param: map
     * @return: java.util.Map
     */
    public Map loginUseFace(Map map) {
        try {
            SysUser user = userMapper.getUserByPhone(map.get("phone_number").toString());
            user.setLogin_ip(Other.getIp());
            user.setLogintime(System.currentTimeMillis() / 1000 + "");
            userMapper.upLogin(user);
            MyToken.verify(map.get("token").toString());
            return Result.success().add("user", user);
        } catch (Exception e) {
            return Result.fail(Code.ACC_ERR, e.toString());
        }
    }

    public Map changePWD(Map map) {
        try {
            SysUser user = userMapper.getUserById(map.get("uid").toString());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            //判断请求中旧密码和用户旧密码是否相同
//            if (!encoder.matches(map.get("password").toString(), user.getPassword())) {
//                return Result.fail(Code.PWD_ERR, "修改失败,旧密码错误");
//            }
            user.setPassword(encoder.encode(map.get("newpassword").toString()));
            userMapper.changePWD(user);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(Code.PWD_ERR, "修改失败");
        }
    }



    public List<String> getAllUser() {
        return userMapper.getAll();
    }

    public List<UserTemp> getRandom10() {
        return userMapper.getRandom10();
    }

    /* 注册账号，参数：phone_number,password,username
     * @param: user
     * @return: java.util.Map
     */
    public Map register(SysUser user) {
        try {
            String password = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(password);
            user.setCreate_time(System.currentTimeMillis() / 1000 + "");
            user.setLogintime(System.currentTimeMillis() / 1000 + "");
            user.setLogin_ip(Other.getIp());
            user.setGender(user.getGender());
            userMapper.register(user);
            return Result.success().add("token", MyToken.create(new MyMap<>().add("uid", user.getUid())));
        } catch (DuplicateKeyException e) {
            return Result.fail(Code.REGISTER_ERR, "注册失败,手机号已存在");
        }
    }

    public Map changeUser(HttpServletRequest request) {
        try {
            MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
            String uid = MyToken.getUid(request);
            SysUser tokenUser = userMapper.getUserById(uid);
            String rqUid = params.getParameter("uid");
            SysUser user = new SysUser();
            if (!rqUid.equals(String.valueOf(tokenUser.getUid()))) {
                throw new Exception();
            }
            String userAvatar = upPics(params, "userAvatar");
            String cover = upPics(params, "cover");
            String bio = params.getParameter("bio");
            String username = params.getParameter("username");
            String gender = params.getParameter("gender");
            String birthday = params.getParameter("birthday");
            String height = params.getParameter("height");
            String weight = params.getParameter("weight");
            user.setUid(tokenUser.getUid());
            user.setCover(cover == null ? tokenUser.getCover() : cover);
            user.setUserAvatar(userAvatar == null ? tokenUser.getUserAvatar() : userAvatar);
            user.setBio(bio == null ? tokenUser.getBio() : bio);
            user.setUsername(username == null ? tokenUser.getUsername() : username);
            user.setGender(gender==null?tokenUser.getGender():Long.parseLong(gender));
            user.setBirthday(birthday == null ? tokenUser.getBirthday() : birthday);
            user.setHeight(height == null ? tokenUser.getHeight() : Integer.parseInt(height));
            user.setWeight(weight == null ? tokenUser.getWeight() : Integer.parseInt(weight));
            log.info(user.toString());
            userMapper.changeUser(user);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "");
        }
    }


    @Value("${articlepics-path}")
    private String articlepicsPath;

    public String upPics(HttpServletRequest request, String name) throws Exception {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(name);
        if (file == null) {
            return null;
        }
        List<String> resultsPath = new ArrayList<>();
        String randomNumber = Other.getRandomNumber(10);
        File newFile = new File(articlepicsPath + "/" + randomNumber + ".jpg");
        file.transferTo(newFile);
        String url =/* request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +*/
                "/api/graduate/articlepics/" + randomNumber + ".jpg";
        resultsPath.add(url);
        String result = resultsPath.toString();
        //list转string后去除首尾[]中括号
        return result.substring(1, result.length() - 1).replaceAll(" ", "");
    }
}
