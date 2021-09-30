package com.tah.graduate_run.service.comment;

import com.tah.graduate_run.entity.Comment;
import com.tah.graduate_run.mapper.CommentMapper;
import com.tah.graduate_run.untils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public Map getCommentList(String fid) {
        List<Comment> list = commentMapper.getArticleComment(fid);
//        log.info(list.toString());
        return Result.success().add("rows", list);
    }
}
