package com.tah.graduate_run.service.article;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tah.graduate_run.config.MyToken;
import com.tah.graduate_run.entity.Article;
import com.tah.graduate_run.entity.UpArticle;
import com.tah.graduate_run.mapper.ArticleMapper;
import com.tah.graduate_run.mapper.UserNumberMapper;
import com.tah.graduate_run.untils.Other;
import com.tah.graduate_run.untils.Result;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/18 18:42
 */
@Service
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Resource
    ArticleMapper mapper;

    @Resource
    UserNumberMapper userNumberMapper;

    public Map likeNumUp(Map map) {
        mapper.likeNumUp(map);
        mapper.insertLike(map);
        return Result.success();
    }

    public Map likeNumDown(Map map) {
        mapper.likeNumDown(map);
        mapper.deleteLike(map);
        return Result.success();
    }
    public Map getArticleInfo(HttpServletRequest request,String id){
        String uid = MyToken.getUid(request);
        List<Long> focus = userNumberMapper.getFocus(uid);
        List<Long> likes = mapper.myLikes(uid);
        Article info = mapper.getArticleInfo(id);
        //判断是否关注过
        if (focus.contains(info.getUid())) {
            info.setFocus(true);
        }
        //判断是否点赞过
        if (likes.contains(info.getId())){
            info.setLike(true);
        }
        //把图片放入picArr集合
        if (!info.getPic().isEmpty()) {
            info.setPicArr(Arrays.asList(info.getPic().split(",")));
        }
        log.info(info.toString());
        return Result.success().add("info", info);
    }

    public Map getArticle(HttpServletRequest request,Integer pagerNum, Integer pagerSize) {
        List<Article> list = mapper.getArticle(pagerNum,pagerSize);
        String uid = MyToken.getUid(request);
        List<Long> likes = mapper.myLikes(uid);
        for (Article article : list) {
            if (likes.contains(article.getId())){
                article.setLike(true);
            }
            try {
                if (!article.getPic().isEmpty()) {
                    article.setPicArr(Arrays.asList(article.getPic().split(",")));
                }
            } catch (Exception e) {

            }
        }
        return Result.success().add("rows", list);
    }

    public Map getUserAllArticle(HttpServletRequest request,String uid){
        List<Article> list = mapper.getUserAllArticle(uid);
        String tokenid = MyToken.getUid(request);
        log.info(tokenid);
        List<Long> likes = mapper.myLikes(tokenid);
        for (Article article : list) {
            if (likes.contains(article.getId())){
                article.setLike(true);
            }
            try {
                if (!article.getPic().isEmpty()) {
                    article.setPicArr(Arrays.asList(article.getPic().split(",")));
                }
            } catch (Exception e) {

            }
        }
        return Result.success().add("rows", list);
    }
    public Map createArticle(HttpServletRequest request) {
        try {
            String urls = upPics(request);
            log.info(urls);
            System.out.println(urls);
            MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
            String json = params.getParameter("article");
            log.info(json);
            JsonMapper jsonMapper = new JsonMapper();
            UpArticle article = jsonMapper.readValue(json, UpArticle.class);
            article.setPic(urls);
            log.info(article.toString()+"92");
            mapper.createArticle(article);
            log.info(article.toString());
            return Result.success().add("id", article.getId());
        } catch (IOException e) {
            return Result.fail(404, "对象错误" + e);
        } catch (Exception e) {
            return Result.fail(404, "图片上传失败" + e);
        }
    }


    @Value("${articlepics-path}")
    private String articlepicsPath;

    public String upPics(HttpServletRequest request) throws Exception {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        if (files.size() == 0) {
            return null;
        }
        List<String> resultsPath = new ArrayList<>();
        for (MultipartFile file : files) {
            String randomNumber = Other.getRandomNumber(10);
            File newFile = new File(articlepicsPath + "/" + randomNumber + ".jpg");
            file.transferTo(newFile);
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                    "/graduate/articlepics/" + randomNumber + ".jpg";
            resultsPath.add(url);
        }
        String result = resultsPath.toString();
        //list转string后去除首尾[]中括号
        return result.substring(1, result.length() - 1);
    }
}
