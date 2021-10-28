package com.tah.graduate_run.service.record;

import com.tah.graduate_run.entity.*;
import com.tah.graduate_run.mapper.RecordMapper;
import com.tah.graduate_run.mapper.SysUserMapper;
import com.tah.graduate_run.mapper.UserRecordMapper;
import com.tah.graduate_run.service.EnterExitSerive;
import com.tah.graduate_run.untils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/10/15 17:59
 */
@Service
public class RecordService {
    @Resource
    RecordMapper recordMapper;
    @Resource
    SysUserMapper sysUserMapper;
    @Resource
    UserRecordMapper userRecordMapper;


    public Map insertRecord(Map map) {
        Long datetime = (Long) map.get("datetime");
        String username = (String) map.get("username");
        Integer pid = (Integer) map.get("pid");
        for (RunUser runUser : EnterExitSerive.beUser) {
            if (runUser.getUsername().equals(username)){
                runUser.u_records.add(new U_Record(datetime, pid));
                log.info(runUser.u_records.toString());
                return Result.success();
            }
        }
        return Result.fail(0, "出错咯");
    }


    private String speedFormat(long n) {
        long b = n % (1000 * 60 * 60) / (1000 * 60);
        long c = n % (1000 * 60 * 60) / 1000;
        return ( (b<10?"0"+b:b) + "'"
                + (c < 100 ? c : String.valueOf(c).substring(0, 2))+ "\"" );
    }

    private String convertMillis(long n) {
        String a = n / (1000 * 60 * 60)+"";
        String b = n / (1000 * 60 )%60+"";
        String c = n /1000%60+"";
        String d = n % 1000+"";

        return ((a.length()<2?"0"+a:a) + ":" + (b.length()<2?"0"+b:b) + ":" + (c.length()<2?"0"+c:c) +
                ":" +(d.length()>2?d.substring(0,2):d));
    }

    public  BigDecimal getSpeed(List<U_Record> list) {/*+ ":" +
                (d.length()<3 ? d : d.substring(0, 2))*/
        if (list.size() == 0) {
            return new BigDecimal(0);
        }
        BigDecimal allLength = new BigDecimal(0);
        for (U_Record u_record : list) {
            Probe probe = recordMapper.getProbe(u_record.getPid());
            Integer offLength = Integer.valueOf(probe.getP_location());
            allLength = allLength.add(new BigDecimal(offLength));
        }
        log.info(">" + allLength.toString());
        BigDecimal scale = allLength.divide(new BigDecimal(1000),4, BigDecimal.ROUND_HALF_UP);
        BigDecimal time = new BigDecimal(Math.abs(list.get(list.size() - 1).getDatetime() - list.get(0).getDatetime()));
        BigDecimal resultTime = time.divide(scale,4, BigDecimal.ROUND_HALF_UP);
        return resultTime;
    }

    private static final Logger log = LoggerFactory.getLogger(RecordService.class);
}
