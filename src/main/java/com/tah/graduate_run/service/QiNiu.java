package com.tah.graduate_run.service;

import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

public class QiNiu {
    // TODO: 2021/8/31 需要将密钥网址和空间设在配置文件内读取
    public String uploadPicture(MultipartFile file) throws Exception {
//        String qiniuUrl = "http://qxawschmv.hd-bkt.clouddn.com/";
        String qiniuUrl = "http://qyl1ts9h7.hn-bkt.clouddn.com/";
//        Configuration cfg = new Configuration(Zone.zone2());
        //华南地区
        Configuration configuration = new Configuration(Region.huanan());
        UploadManager uploadManager = new UploadManager(configuration);
        String accessKey = "Top2c-19iZpJw0UoyRAMTpqCjcoCA3jLZT6jZnzw";
        String secretKey = "DTkm9o84bMrJLHQYSNoLP-aY0duv6mrbNSmaAlM1";
        String bucket = "tanghan";
        String key = getRandomNumber(10) + ".png";//生成随机文件名
        Auth auth = Auth.create(accessKey, secretKey);
        String uptoken = auth.uploadToken(bucket);
        String responseUrl = "";
        byte[] localFile = file.getBytes();
        Response response = uploadManager.put(localFile, key, uptoken);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        responseUrl = responseUrl + qiniuUrl + putRet.key;
        return responseUrl;
    }

    public static String getRandomNumber(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }
}
