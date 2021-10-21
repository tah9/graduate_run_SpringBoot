package com.tah.graduate_run.service.record;

import com.tah.graduate_run.entity.U_Record;
import com.tah.graduate_run.entity.User_Record;
import com.tah.graduate_run.mapper.UserRecordMapper;
import com.tah.graduate_run.untils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ->  tah9  2021/10/18 15:38
 */
@Service
public class UserRecordService {

    @Resource
    UserRecordMapper userRecordMapper;

    public Map getMaxSpeed(){
        List<User_Record> allUserRecord = userRecordMapper.getMaxSpeed();
        //用户记录去重，保留最快速度
        List<User_Record> result = new ArrayList<>(allUserRecord.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User_Record::getUid)))));
        result.sort((o1, o2) -> (int) (o1.getSpeed() - o2.getSpeed()));
        return Result.success(result);
    }

    public Map getMaxTime(){
        List<User_Record> allRecord = userRecordMapper.getAllRecord();
        Iterator<User_Record> iterator = allRecord.iterator();
        List<User_Record> resultList = new ArrayList<>();
        while (iterator.hasNext()) {
            User_Record next = iterator.next();
            iterator.remove();
            long time = next.getTime();
            Iterator<User_Record> i1 = allRecord.iterator();
            while (i1.hasNext()) {
                User_Record n1 = i1.next();
                if (n1.getUid()==next.getUid()){
                    time += n1.getTime();
//                    i1.remove();
                }
            }
            resultList.add(new User_Record(next.getUid(),time,next.getUserAvatar()));
        }
        resultList.sort((o1, o2) -> (int) (o2.getTime() - o1.getTime()));
        //用户记录去重，保留最长时间
        List<User_Record> result = new ArrayList<>(resultList.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User_Record::getUid)))));
        return Result.success(result);
    }
}
