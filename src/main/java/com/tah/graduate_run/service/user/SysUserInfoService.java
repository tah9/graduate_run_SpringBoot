package com.tah.graduate_run.service.user;

import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.mapper.SysUserMapper;
import com.tah.graduate_run.untils.MyMap;
import com.tah.graduate_run.untils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ->  tah9  2021/9/22 10:03
 */
@Service
public class SysUserInfoService {
private static final Logger log= LoggerFactory.getLogger(SysUserInfoService.class);
    @Resource
    SysUserMapper mapper;

    public int byThumbUpCount(String aid){
        int count = mapper.byThumbUpCount(aid);
        System.out.println(count);
        return count;
    }

    public MyMap getUserInfo(String id){
        try {
            SysUser user = mapper.getUserInfo(id);
            log.info(user.toString());
            int focusOn=mapper.getFocusOnSum(id);
            log.info(focusOn + "");
            int fans = mapper.getFansSum(id);
            log.info(fans + "");
            int likes = mapper.getLikesSum(id);
            log.info(likes + "");

            return Result.success().add("focus",focusOn).add("fans",fans).add("likes",likes)
                    .add("user",user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500,"用户不存在");
        }
    }
}
