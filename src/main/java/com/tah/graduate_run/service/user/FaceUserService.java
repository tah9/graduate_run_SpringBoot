package com.tah.graduate_run.service.user;

import com.tah.graduate_run.service.WebSocketService;
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
 * ->  tah9  2021/10/6 8:32
 */
@Service
public class FaceUserService {
    @Value("${face-file-path}")
    private String faceFilePath;

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
        File userFolder = new File(faceFilePath +username);
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
}
