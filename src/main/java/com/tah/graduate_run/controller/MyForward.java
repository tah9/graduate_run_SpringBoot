package com.tah.graduate_run.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * ->  tah9  2021/10/3 16:37
 */
@RestController
@RequestMapping("/proxy")
public class MyForward {



    @RequestMapping(value = "/get")
    public String get(@RequestParam(name = "url",required = true)String url){
        RestTemplate rest = new RestTemplate();
        url = "https://api.mapbox.com/styles/v1/mapbox/streets-v11?access_token=pk.eyJ1IjoidGFoOSIsImEiOiJja3YzZWVuengxMDY5Mm5vdnFpazY5dmVtIn0.VE-xqWk-iVhUeR13Xl6bQQ";
        ResponseEntity<String> responseEntity = rest.getForEntity(url, String.class);
        return responseEntity.getBody();
    }

    @RequestMapping(value = "/forward")
    public ResponseEntity<Resource> forward(@RequestParam(name = "url", required = true) String url){

        Resource resource = null;

        InputStream inputStream;
        try {
            inputStream = new URL(url).openStream();

            resource = new ByteArrayResource(toByteArray(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, " filename=\"" + getFileName(url) + "\"")
                .body(resource);
    }


    private byte[] toByteArray(InputStream in){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bytes = new byte[1024];
            int n;
            while ((n = in.read(bytes)) != -1){
                byteArrayOutputStream.write(bytes, 0, n);
            }
            System.out.println(byteArrayOutputStream.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private String getFileName(String url){
        String[] strs = url.split("/");
        for (String str: strs){
            // 这里我仅处理png格式的，根据需求修改即可
            if (str.toLowerCase().endsWith(".png")){
                return str;
            }
//            return str;
        }
        return null;
    }

}
