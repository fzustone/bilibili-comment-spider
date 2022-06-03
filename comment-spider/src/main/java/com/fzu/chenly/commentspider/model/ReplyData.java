package com.fzu.chenly.commentspider.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chenly
 * @create 2022-05-29 11:16
 */
@Data
public class ReplyData {
    private int mid;
    private Long ctime;


    private Member member;
    private ReplyContent content;
    /**
     * 楼中楼
     */
    //private ReplyData replies;

    @JsonProperty("reply_control")
    private ReplyControl replyControl;
}
