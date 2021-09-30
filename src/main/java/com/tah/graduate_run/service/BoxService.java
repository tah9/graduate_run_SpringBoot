package com.tah.graduate_run.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ->  tah9  2021/9/20 18:03
 */
@Service
public class BoxService {
    @Value("${box-path}")
    private String boxPath;

    public void inputFace(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        String name = params.getParameter("name");
        File folder = new File(boxPath + name);
        folder.mkdirs();
        int index = 0;
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        for (MultipartFile file : files) {
            File newFile = new File(boxPath + name + "/" + index + ".jpg");
            file.transferTo(newFile);
        }
    }

    public List<String> getFace() {
        List<String> result = new ArrayList<>();
        File folder = new File(boxPath);
        for (File file : folder.listFiles()) {
            result.add(file.getName());
        }
        return result;
    }

    public void deleteFace(String name) {
        try {
            File folder = new File(boxPath + name);
            FileUtils.deleteDirectory(folder);
        } catch (Exception e) {

        }
    }
}
