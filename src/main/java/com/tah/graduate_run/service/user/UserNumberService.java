package com.tah.graduate_run.service.user;

import com.tah.graduate_run.mapper.UserNumberMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ->  tah9  2021/9/25 13:17
 */
@Service
public class UserNumberService {
    @Resource
    UserNumberMapper mapper;

    public List<Long> getFocus(String uid){
        return mapper.getFocus(uid);
    }
}
