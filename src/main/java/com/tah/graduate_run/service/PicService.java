package com.tah.graduate_run.service;

import com.tah.graduate_run.untils.Code;
import com.tah.graduate_run.untils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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

    public Map uptoQiNiu(MultipartFile file) {
        try {
            String url = new QiNiu().uploadPicture(file);
            System.out.println(url);
            return Result.success().add("url", url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(Code.UPQI_ERR, e.toString());
        }
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

        int index = 0;
        // 检测用户目录是否存在
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }//起始下标延续已有人脸数
        else {
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
            return Result.fail(404, e.toString());

        }

    }
}
