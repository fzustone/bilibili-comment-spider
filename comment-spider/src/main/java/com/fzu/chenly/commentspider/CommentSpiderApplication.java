package com.fzu.chenly.commentspider;

import com.fzu.chenly.commentspider.service.CommentSpiderService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fzu.chenly.commentspider.mapper")
public class CommentSpiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommentSpiderApplication.class, args);
    }

}
