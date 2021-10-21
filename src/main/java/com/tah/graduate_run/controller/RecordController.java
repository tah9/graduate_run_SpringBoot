package com.tah.graduate_run.controller;

import com.tah.graduate_run.mapper.UserRecordMapper;
import com.tah.graduate_run.service.record.RecordService;
import com.tah.graduate_run.service.record.UserRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * ->  tah9  2021/10/15 18:02
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    RecordService recordService;

    @Resource
    UserRecordService userRecordService;


    @PostMapping("/insert")
    public Map insertRecord(@RequestBody Map map){
        return recordService.insertRecord(map);
    }

    @GetMapping("/get")
    public Map getRecord(@RequestParam String username){
        return recordService.getRecord(username);
    }

    @GetMapping("/getMaxSpeed")
    public Map getUserRecord(){
        return userRecordService.getMaxSpeed();
    }

    @GetMapping("/getMaxTime")
    public Map getMaxTime(){
        return userRecordService.getMaxTime();
    }
}
