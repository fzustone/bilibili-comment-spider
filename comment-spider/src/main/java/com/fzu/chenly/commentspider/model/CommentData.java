package com.fzu.chenly.commentspider.model;

import lombok.Data;

import java.util.List;

/**
 * @author chenly
 * @create 2022-05-29 11:15
 */
@Data
public class CommentData {
    private List<ReplyData> replies;

}
