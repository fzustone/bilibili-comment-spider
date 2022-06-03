package com.fzu.chenly.commentspider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fzu.chenly.commentspider.model.dao.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author chenly
 * @create 2022-05-29 14:52
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    int batchInsert(@Param("collect") List<Comment> collect);
}