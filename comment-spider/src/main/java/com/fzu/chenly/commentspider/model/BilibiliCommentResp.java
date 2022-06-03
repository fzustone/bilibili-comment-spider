package com.fzu.chenly.commentspider.model;

import lombok.Data;

/**
 * @author chenly
 * @create 2022-05-29 11:14
 */
@Data
public class BilibiliCommentResp {
    private int code;
    private String message;
    private int ttl;
    private CommentData data;
}
