package com.tah.graduate_run.service.article;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tah.graduate_run.config.MyToken;
import com.tah.graduate_run.entity.Article;
import com.tah.graduate_run.entity.Comment;
import com.tah.graduate_run.mapper.ArticleMapper;
import com.tah.graduate_run.mapper.CommentMapper;
import com.tah.graduate_run.mapper.UserNumberMapper;
import com.tah.graduate_run.untils.MyMap;
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
    CommentMapper commentMapper;
    @Resource
    UserNumberMapper userNumberMapper;

    public Map likeNumUp(Map map) {
        String type = map.get("type").toString();
        if (type.equals("comment")) {
            userNumberMapper.commentLikeNum(map);
        } else {
            userNumberMapper.likeNumUp(map);
        }
        userNumberMapper.insertLike(map);
        return Result.success();
    }

    public Map likeNumDown(Map map) {
        String type = map.get("type").toString();
        if (type.equals("comment")) {
            userNumberMapper.commentLikeNumDown(map);
        } else {
            userNumberMapper.likeNumDown(map);
        }
        userNumberMapper.deleteLike(map);
        return Result.success();
    }


    public Map getArticleInfo(HttpServletRequest request, String id) {
        String uid = MyToken.getUid(request);
        List<Long> focus = userNumberMapper.getFocus(uid, "user");
        List<Long> likes = userNumberMapper.myLikes("article", uid);
        Article info = mapper.getArticleInfo(id);
        //判断是否关注过
        info.setBeFollow(focus.contains(info.getUid()));
        //判断是否点赞过
        info.setBeLike(likes.contains(info.getId()));
        log.info(info.toString());
        return Result.success().add("info", info);
    }

    public Map getArticle(HttpServletRequest request,
                          String searchTag, String searchUid,
                          String feedType, String orderKey,
                          Integer pagerNum, Integer pagerSize) {
        //指定用户
        if (!searchUid.isEmpty()) {
            searchUid = "and a.`uid`=" + searchUid;
        }
        //指定话题
        if (!searchTag.isEmpty()) {
            searchTag = "and a.`tags` like'#" + searchTag + "#'";
        }
        log.info(searchUid);
        List<Article> list = mapper.getArticle(searchTag, searchUid, feedType, orderKey, pagerNum, pagerSize);
        String uid = MyToken.getUid(request);

        List<Long> likes = userNumberMapper.myLikes("article", uid);
        for (Article article : list) {
            article.setBeLike(likes.contains(article.getId()));
            String hot_ids = article.getRecent_hot_reply_ids();
            if (hot_ids != null && !hot_ids.isEmpty()) {
                String firstId = hot_ids.split(",")[0];
                Comment firstComment = commentMapper.getFirstComment(firstId);
                article.setFirstComment(firstComment);
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
            Article article = jsonMapper.readValue(json, Article.class);
            article.setPic(urls);
            log.info(article.toString() + "92");
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
            String url =/* request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +*/
                    "/api/graduate/articlepics/" + randomNumber + ".jpg";
            resultsPath.add(url);
        }
        String result = resultsPath.toString();
        //list转string后去除首尾[]中括号
        return result.substring(1, result.length() - 1).replaceAll(" ", "");
    }
}
