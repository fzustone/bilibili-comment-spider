package com.fzu.chenly.commentspider.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chenly
 * @create 2022-05-29 11:30
 */
@Data
public class ReplyControl {
    /**
     * sample:{
     *           "up_reply": true,
     *           "sub_reply_entry_text": "共119条回复",
     *           "sub_reply_title_text": "相关回复共119条",
     *           "time_desc": "4天前发布"
     *         }
     */
    @JsonProperty("sub_reply_entry_text")
    private String subReplyEntryText;

}
