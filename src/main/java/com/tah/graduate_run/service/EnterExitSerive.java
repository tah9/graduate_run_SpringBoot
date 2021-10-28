package com.tah.graduate_run.service;

import com.tah.graduate_run.entity.Probe;
import com.tah.graduate_run.entity.RunUser;
import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.entity.U_Record;
import com.tah.graduate_run.mapper.RecordMapper;
import com.tah.graduate_run.mapper.SysUserMapper;
import com.tah.graduate_run.mapper.UserRecordMapper;
import com.tah.graduate_run.untils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * ->  tah9  2021/9/9 10:11
 */
@Service
public class EnterExitSerive {

    @Resource
    SysUserMapper sysUserMapper;
    @Resource
    RecordMapper recordMapper;

    @Resource
    UserRecordMapper userRecordMapper;

    public static List<RunUser> beUser = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(EnterExitSerive.class);

    public Map enterUser(Map map) {
        String username = (String) map.get("username");
        Long datetime = (Long) map.get("datetime");
        Integer pid = (Integer) map.get("pid");
        //检查是否已填加
        for (RunUser runUser : beUser) {
            if (runUser.getUsername().equals(username)) {
                return Result.fail(0, "此人脸已加入");
            }
        }
        log.info(map.toString());
        SysUser user = sysUserMapper.getUserInfo(username);
        RunUser runUser = new RunUser(username, user.getUid());
        runUser.u_records.add(new U_Record(datetime,pid));
        beUser.add(runUser);

        log.info(beUser.toString());
        return Result.success();
    }

    public Map exitUser(Map map) {
        String username = (String) map.get("username");
        Long datetime = (Long) map.get("datetime");
        Integer pid = (Integer) map.get("pid");
        Iterator<RunUser> iterator = beUser.iterator();
        while (iterator.hasNext()) {
            RunUser runUser = iterator.next();
            if (runUser.getUsername().equals(username)) {
                insertRecord(runUser);
                iterator.remove();
                break;
            }
        }
        log.info(beUser.toString());
        return Result.success();
    }

    public void insertRecord(RunUser runUser) {
        List<U_Record> u_records = runUser.getU_records();
        u_records.sort(new Comparator<U_Record>() {
            @Override
            public int compare(U_Record o1, U_Record o2) {
                return Integer.parseInt((o1.getDatetime() - o2.getDatetime()) + "");
            }
        });
        BigDecimal[] speedAndTime = getSpeedAndTime(u_records);
        BigDecimal speed = speedAndTime[0];
        BigDecimal length = speedAndTime[1];
        Long time = Math.abs(u_records.get(u_records.size() - 1).getDatetime() - u_records.get(0).getDatetime());
        List<Integer> resultJson = new ArrayList<>();
        for (U_Record record : u_records) {
            resultJson.add(record.getPid());
        }
        String json = resultJson.toString().replaceAll(" ", "");
        log.info(json);
        userRecordMapper.insertUserRecord(runUser.getUid(),speed.longValue(),
                u_records.get(0).getDatetime(), time,
                json,
                length.intValue());
    }

    public BigDecimal[] getSpeedAndTime(List<U_Record> list) {
        if (list.size() == 0) {
            return new BigDecimal[]{new BigDecimal(0), new BigDecimal(0)};
        }
        BigDecimal allLength = new BigDecimal(0);
        for (U_Record u_record : list) {
            Probe probe = recordMapper.getProbe(u_record.getPid());
            Integer offLength = Integer.valueOf(probe.getP_location());
            allLength = allLength.add(new BigDecimal(offLength));
        }
        log.info(">" + allLength.toString());

        BigDecimal scale = allLength.divide(new BigDecimal(1000), 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal time = new BigDecimal(Math.abs(list.get(list.size() - 1).getDatetime() - list.get(0).getDatetime()));
        BigDecimal resultTime = time.divide(scale, 4, BigDecimal.ROUND_HALF_UP);
        return new BigDecimal[]{resultTime, allLength};
    }
}
