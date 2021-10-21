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

    public Map enterUser(String username) {
        for (RunUser runUser : beUser) {
            if (runUser.getUsername().equals(username)) {
                return Result.fail(000, "");
            }
        }
        SysUser user = sysUserMapper.getUserInfo(username);
        Integer belonging = recordMapper.getBelonging(user.getUid());
        if (belonging == null) {
            belonging = 0;
        }
        RunUser runUser = new RunUser(username, user.getUid(), belonging + 1);
        beUser.add(runUser);
        log.info(beUser.toString());
        return Result.success();
    }

    public Map exitUser(String userName) {

        Iterator<RunUser> iterator = beUser.iterator();
        while (iterator.hasNext()) {
            RunUser runUser = iterator.next();
            if (runUser.getUsername().equals(userName)) {
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
        userRecordMapper.insertUserRecord(runUser.getUid(),speed.longValue(),
                u_records.get(0).getDatetime(), time,
                u_records.toString(),length.intValue());
    }

    public BigDecimal[] getSpeedAndTime(List<U_Record> list) {
        if (list.size() == 0) {
            return new BigDecimal[]{new BigDecimal(0), new BigDecimal(0)};
        }
        BigDecimal allLength = new BigDecimal(0);
        for (int i = 1; i < list.size(); i++) {
            U_Record oRecord = list.get(i - 1);
            U_Record nRecord = list.get(i);
            Probe oProbe = recordMapper.getProbe(oRecord.getPid());
            Probe nProbe = recordMapper.getProbe(nRecord.getPid());
            Integer offLength = Math.abs(Integer.parseInt(nProbe.getP_location()) -
                    Integer.parseInt(oProbe.getP_location()));
            log.info(i + ">>" + offLength);
            allLength = allLength.add(new BigDecimal(offLength));
        }
        log.info(">" + allLength.toString());
        BigDecimal scale = allLength.divide(new BigDecimal(1000), 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal time = new BigDecimal(Math.abs(list.get(list.size() - 1).getDatetime() - list.get(0).getDatetime()));
        BigDecimal resultTime = time.divide(scale, 4, BigDecimal.ROUND_HALF_UP);
        return new BigDecimal[]{resultTime, allLength};
    }
}
