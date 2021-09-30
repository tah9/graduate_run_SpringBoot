package com.tah.graduate_run.service;

import com.tah.graduate_run.mapper.SysUserMapper;
import com.tah.graduate_run.untils.Code;
import com.tah.graduate_run.untils.MyMap;
import com.tah.graduate_run.untils.Result;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/8/31 16:34
 */
@Service
public class PicService {
    @Resource
    RedisTemplate<String, Object> redis;

    @Resource
    SysUserMapper userMapper;


    public Map uptoQiNiu(MultipartFile file) {
//        try {
//            String url = new QiNiu().uploadPicture(file);
//            System.out.println(url);
//            return Result.success().add("url", url);
//        } catch (Exception e) {
//            e.printStackTrace();
            return Result.fail(Code.UPQI_ERR, "e.toString()");
//        }
    }


    @Value("${face-file-path}")
    private String faceFilePath;

    /* {file:file...
       username:folder} //每个用户名为目录存放人脸图片
       此接口接收表单，图片为文件形式（非base64），支持多图上传
     * @param: request
     * @return: java.util.Map
     */
    public Map uptoUserFolder(HttpServletRequest request) {
        //获取参数
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        String username = params.getParameter("username");
        File userFolder = new File(faceFilePath + username);

        MyMap map = new MyMap().add("username",username);
        int index = 0;
        // 检测用户目录是否存在
        if (!userFolder.exists()) {
            userFolder.mkdirs();
            userMapper.insertTourist(username);
        }//起始下标延续已有人脸数
        else {
            index = userFolder.list().length;
            map.add("update", true);
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

            //写入文件夹后，通知探头加入此用户人脸信息
            WebSocketService.sendMessage("EnterExit", map);
            return Result.success().add("urlList", resultsPath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(404, e.toString());

        }

    }
}
