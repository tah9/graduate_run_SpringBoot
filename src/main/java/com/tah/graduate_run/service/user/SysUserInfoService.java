package com.tah.graduate_run.service.user;

import com.tah.graduate_run.config.MyToken;
import com.tah.graduate_run.entity.Article;
import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.mapper.SysUserMapper;
import com.tah.graduate_run.mapper.UserNumberMapper;
import com.tah.graduate_run.untils.MyMap;
import com.tah.graduate_run.untils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ->  tah9  2021/9/22 10:03
 */
@Service
public class SysUserInfoService {
    private static final Logger log = LoggerFactory.getLogger(SysUserInfoService.class);
    @Resource
    SysUserMapper mapper;

    @Resource
    UserNumberMapper userNumberMapper;

    public int byThumbUpCount(String aid) {
//        int count = mapper.byThumbUpCount(aid);
        int count = 0;
        System.out.println(count);
        return count;
    }


    public MyMap getUserInfo(HttpServletRequest request, Map map) {
        try {
            SysUser user = mapper.getUserInfo(map.get("username") + "");
            String uid = MyToken.getUid(request);
            List<Long> focus = userNumberMapper.getFocus(uid,"user");
            //判断是否关注过
            user.setBeFollow(focus.contains(user.getUid()));
            return Result.success().add("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "用户不存在");
        }
    }
}
