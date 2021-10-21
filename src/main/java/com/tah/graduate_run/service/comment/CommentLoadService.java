package com.tah.graduate_run.service.comment;

import com.tah.graduate_run.config.MyToken;
import com.tah.graduate_run.entity.Article;
import com.tah.graduate_run.entity.Comment;
import com.tah.graduate_run.mapper.CommentMapper;
import com.tah.graduate_run.mapper.UserNumberMapper;
import com.tah.graduate_run.service.article.ArticleService;
import com.tah.graduate_run.untils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ->  tah9  2021/9/28 8:20
 */
@Service
public class CommentLoadService {
    private static final Logger log = LoggerFactory.getLogger(CommentLoadService.class);

    @Resource
    CommentMapper commentMapper;
    @Resource
    UserNumberMapper userNumberMapper;
    @Resource
    ArticleService articleService;


    public Map getCommentList(HttpServletRequest request, String fid) {
        String uid = MyToken.getUid(request);
        List<Comment> list = commentMapper.getArticleComment(fid);
        List<Integer> commentLikes = userNumberMapper.myLikes("comment", uid);
        for (int i = 0; i < list.size(); i++) {
            Comment comment = list.get(i);
            comment.setBeLike(commentLikes.contains(comment.getId()));
        }
        return Result.success().add("rows", list);
    }

    public Map pushComment(HttpServletRequest request) {
        try {
            String uid = MyToken.getUid(request);
            String s = articleService.upPics(request);
            Comment comment = new Comment();
            comment.setPic(s);
            MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
//            fid,uid,rid,rrid,message,dateline,pic,rusername,isFeedAuthor
            comment.setFid(Integer.parseInt(params.getParameter("fid")));
            comment.setUid(Integer.parseInt(uid));
            comment.setRid(Integer.parseInt(params.getParameter("rid")));
            comment.setRrid(Integer.parseInt(params.getParameter("rrid")));
            comment.setMessage(params.getParameter("message"));
            comment.setDateline(Integer.parseInt(params.getParameter("dateline")));
            comment.setRusername(params.getParameter("rusername"));
            comment.setIsFeedAuthor(Integer.parseInt(params.getParameter("isFeedAuthor")));
            log.info(comment.toString());
            commentMapper.insertComment(comment);
            log.info(comment.getId()+"<<");
            //更新回复的评论数

            //直接回复动态
            if (comment.getRrid()==0){
                //只修改动态评论数
            }
            //回复评论
            else {
                //rrid根评论id
                String reply_ids = commentMapper.queryReplyIds(comment.getRrid());
                List<String> list = new ArrayList<>();
                if (reply_ids!=null){
                    //加入已有的热评id
                    list.addAll(Arrays.asList(reply_ids.split(",")));
                    list.remove("");
                }
                log.info(list.size()+"");
                if (list.size()<=5){
                    list.add(comment.getId() + "");
                }else{

                }
                String result = list.toString()
                        .replaceAll(" ","")
                        .replaceAll("\\[", "")
                        .replaceAll("]", "");
                log.info(result);
                commentMapper.updateReplyIds(result,comment.getRrid());
                //回复一级评论
                if (comment.getRrid()==comment.getRid()){

                }
                //回复二级评论
                else{
                    //修改二级评论数
                    commentMapper.updateCommentReplyNum(comment.getRid());
                }
                //修改一级评论数
                commentMapper.updateCommentReplyNum(comment.getRrid());
            }
            //更新动态评论数
            commentMapper.updateArticleComment(comment.getFid());
            return Result.success(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, e.toString());
        }
    }
}
