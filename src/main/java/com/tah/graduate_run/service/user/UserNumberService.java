package com.tah.graduate_run.service.user;

import com.tah.graduate_run.config.MyToken;
import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.mapper.UserNumberMapper;
import com.tah.graduate_run.untils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/25 13:17
 */
@Service
public class UserNumberService {
    @Resource
    UserNumberMapper mapper;

    public Map getFocus(String uid, int pagerNum, int pagerSize){
        return Result.success().add("rows", mapper.getFollowUser(uid, pagerNum, pagerSize));
    }

    private static final Logger log= LoggerFactory.getLogger(UserNumberService.class);
    public Map toFocus(Map map) {
        mapper.toFocus(map);
        return Result.success();
    }

    public Map unFocus(Map map) {
        mapper.unFoucs(map);
        return Result.success();
    }

    public Map articleSum(HttpServletRequest request) {
        String uid = MyToken.getUid(request);
        return Result.success().add("sum", mapper.articleSum(uid));
    }
}
