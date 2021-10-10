package com.tah.graduate_run.controller;

import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.service.user.UserNumberService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/25 13:19
 */
@RestController
@RequestMapping("/user")
public class UserNumberController {

    @Resource
    UserNumberService service;

    //查看关注列表
    @GetMapping("/allFollow/{uid}")
    Map userNumber(@PathVariable("uid") String uid,
                   @RequestParam(defaultValue = "1") Integer pagerNum,
                   @RequestParam(defaultValue = "10") Integer pagerSize) {
        return service.getFocus(uid, (pagerNum - 1) * pagerSize, pagerSize);
    }

    @GetMapping("/articleSum")
    Map articleSum(HttpServletRequest request){
        return service.articleSum(request);
    }


    @PostMapping("/focus")
    Map toFocus(@RequestBody Map map) {
        return service.toFocus(map);
    }

    @DeleteMapping("/focus")
    Map unFocus(@RequestBody Map map) {
        return service.unFocus(map);
    }
}
