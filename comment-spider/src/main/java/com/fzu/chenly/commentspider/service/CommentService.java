package com.fzu.chenly.commentspider.service;

import com.fzu.chenly.commentspider.mapper.CommentMapper;
import com.fzu.chenly.commentspider.model.dao.Comment;
import com.fzu.chenly.commentspider.util.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenly
 * @create 2022-05-29 22:57
 */
@Service
@Slf4j
public class CommentService {
    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public void find() {
        final List<Comment> comments = commentMapper.selectList(null);
        final List<Comment> collect = comments.stream()
                .filter(comment -> StringUtils.containsAny(comment.getMessage(), "莆田", "厦门", "福州", "福建"))
                .filter(comment -> StringUtils.equalsAny(comment.getSex(), "保密", "女"))
                .filter(comment -> StringUtils.contains(comment.getMessage(), "女"))
                .filter(comment -> !StringUtils.containsAny(comment.getMessage(), "性别：男", "性别 男", "性别:男"))
                .collect(Collectors.toList());
        fileWrite(JsonUtils.toJson(collect));
    }

    @SneakyThrows
    public String fileWrite(String json) {
        Path path = Paths.get("D:\\project\\bilibili-comment-spider\\comment-spider\\src\\main\\resources\\static\\resp.json");
        //创建文件
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Use try-with-resource to get auto-closeable writer instance
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.flush();
            writer.write(json);
        }
        return "over";
    }

}
