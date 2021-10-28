package com.tah.graduate_run.service.user;

import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.entity.User_Record;
import com.tah.graduate_run.mapper.SysUserMapper;
import com.tah.graduate_run.mapper.UserRecordMapper;
import com.tah.graduate_run.service.WebSocketService;
import com.tah.graduate_run.untils.Code;
import com.tah.graduate_run.untils.Result;
import com.tah.graduate_run.untils.TimeUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ->  tah9  2021/10/6 8:32
 */
@Service
public class FaceUserService {
    @Value("${face-file-path}")
    private String faceFilePath;

    @Resource
    SysUserMapper sysUserMapper;
    @Resource
    UserRecordMapper userRecordMapper;

    public Map getAllUser() {
        List<String> list = new ArrayList<>();
        File file = new File(faceFilePath);
        for (File listFile : file.listFiles()) {
            System.out.println(listFile.getName());
            list.add(listFile.getName());
        }
        return Result.success().add("rows", list);
    }


    public Map addFace(HttpServletRequest request, String username) {
        int index = 0;
        File userFolder = new File(faceFilePath + username);
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        } else {
            index = userFolder.list().length;
        }
        //获取文件，注意这里不可以从params参数获取
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        try {
            List<String> resultsPath = new ArrayList<>();
            for (MultipartFile file : files) {
                File newFile = new File(faceFilePath + username + "/" + index + ".jpg");
                file.transferTo(newFile);
                String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                        "/graduate/face/" + username + "/" + (index++) + ".jpg";
                resultsPath.add(url);
            }
            return Result.success().add("urlList", resultsPath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(Code.ADFACE_ERR, e.toString());
        }

    }

    public Map getRows(Long startTime,Long endTime,List<User_Record> Record){
        List<Float> lengthList = new ArrayList<>();
        List<Float> timeList = new ArrayList<>();
        Map resultMap = new HashMap();
        //每天86400000毫秒
        for (Long i = startTime; i < endTime; i+=86400000) {
            Float length = 0F;
            Float time = 0F;
            for (User_Record record : Record) {
                if (record.getDatetime()>=i&&record.getDatetime()<i+86400000){
                    length += Float.parseFloat(String.format("%.2f",record.getLength()/1000f));
                    time += Float.parseFloat(String.format("%.2f",record.getTime()/1000/60/60f));
                }
            }
            lengthList.add(length);
            timeList.add(time);
        }
        resultMap.put("length", lengthList);
        resultMap.put("time", timeList);
        return resultMap;
    }

    public Map getRunMessage(Map map) {
        String username = map.get("username").toString();
        SysUser user = sysUserMapper.getUserByName(username);
        //查询本月的记录
        Long monthStartTime = TimeUntil.getMonthStartTime(System.currentTimeMillis(), "GMT+8:00");
        Long dayStartTime = TimeUntil.getDayStartTime();
        Long weekStartTime = TimeUntil.getWeekStartTime();

        //今日运动数据
        List<User_Record> todayRecord = new ArrayList<>();
        List<User_Record> weekRecord = new ArrayList<>();
        List<User_Record> monthRecord = new ArrayList<>();
        List<User_Record> userRecord = userRecordMapper.getUserRecord(user.getUid(), monthStartTime);
        BigDecimal todayLength = new BigDecimal(0);
        BigDecimal todayTime = new BigDecimal(0);
        BigDecimal weekLength = new BigDecimal(0);
        BigDecimal weekTime = new BigDecimal(0);
        BigDecimal monthLength = new BigDecimal(0);
        BigDecimal monthTime = new BigDecimal(0);
        for (User_Record record : userRecord) {
            //今日运动数据
            if (record.getDatetime() >= dayStartTime) {
                todayRecord.add(record);
                todayLength = todayLength.add(new BigDecimal(record.getLength()));
                todayTime = todayTime.add(new BigDecimal(record.getTime()));
            }
            //本周数据
            if (record.getDatetime() >= weekStartTime) {
                weekRecord.add(record);
                weekLength = weekLength.add(new BigDecimal(record.getLength()));
                weekTime = weekTime.add(new BigDecimal(record.getTime()));
            }
            //本月数据
            if (record.getDatetime() >= monthStartTime) {
                monthRecord.add(record);
                monthLength = monthLength.add(new BigDecimal(record.getLength()));
                monthTime = monthTime.add(new BigDecimal(record.getTime()));
            }
        }
        Map resultMap = new HashMap();
        resultMap.put("user", user);

        resultMap.put("today", getData(todayLength,todayTime,user));
        resultMap.put("week", getData(weekLength, weekTime, user));
        resultMap.put("weekRows", getRows(weekStartTime,weekStartTime+7*86400000,weekRecord));
        resultMap.put("month", getData(monthLength, monthTime, user));
        resultMap.put("monthRows", getRows(monthStartTime,TimeUntil.getMonthEndTime(System.currentTimeMillis(), "GMT+8:00"),monthRecord));
        return Result.success(resultMap);
        //今日运动数据
    }

    /**
     * 获取kcal，能量代谢当量，里程，时长，跑完全马所需时间。
     * 时间均为毫秒
     * @param length
     * @param time
     * @param user
     * @return
     */
    public Map getData(BigDecimal length,BigDecimal time,SysUser user){
        Map map = new HashMap();
        //距离米转Km
        length = length.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);
        map.put("length", length.floatValue());

        BigDecimal todayKcal = new BigDecimal(user.getWeight()).multiply(length).multiply(new BigDecimal("1.036"));
        map.put("kcal", todayKcal.intValue());
        BigDecimal hour = new BigDecimal((time.longValue() / 1000) + "").divide(new BigDecimal("3600"), 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal metabolic = todayKcal.divide(new BigDecimal(user.getWeight() + "").multiply(hour), 2, BigDecimal.ROUND_HALF_UP);
        map.put("metabolic", metabolic.floatValue());
        map.put("time", time.intValue());
        BigDecimal speed = getSpeed(time, length);
        map.put("speed", speed.intValue());
        BigDecimal ma = speed.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("42195"));
        map.put("ma", ma.intValue());
        return map;
    }
    //todo 后端返回时间均为毫秒

    /**
     * 跑完1000米所需时间
     *
     * @param time
     * @param length
     * @return
     */
    public BigDecimal getSpeed(BigDecimal time, BigDecimal length) {
        BigDecimal scale = length.divide(new BigDecimal(1000), 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal speed = time.divide(scale, 4, BigDecimal.ROUND_HALF_UP);
        return speed.divide(new BigDecimal("1000"), 4, BigDecimal.ROUND_HALF_UP);
    }

    private static final Logger log = LoggerFactory.getLogger(FaceUserService.class);
}
