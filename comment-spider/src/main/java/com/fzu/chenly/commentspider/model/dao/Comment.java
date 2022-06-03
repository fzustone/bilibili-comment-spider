package com.fzu.chenly.commentspider.model.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * ${DESCRIPTION}
 *
 * @author chenly
 * @create 2022-05-29 14:52
 */

@Data
@TableName(value = "`comment`")
public class Comment {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "mid")
    private Long mid;

    @TableField(value = "uname")
    private String uname;

    @TableField(value = "sex")
    private String sex;

    @TableField(value = "current_level")
    private Integer currentLevel;

    /**
     * 多少条
     */
    @TableField(value = "sub_reply_entry_text")
    private String subReplyEntryText;

    /**
     * 回复内容
     */
    @TableField(value = "message")
    private String message;

    @TableField(value = "ctime")
    private String ctime;

    public static final String COL_ID = "id";

    public static final String COL_MID = "mid";

    public static final String COL_UNAME = "uname";

    public static final String COL_SEX = "sex";

    public static final String COL_CURRENT_LEVEL = "current_level";

    public static final String COL_SUB_REPLY_ENTRY_TEXT = "sub_reply_entry_text";

    public static final String COL_MESSAGE = "message";

    public static final String COL_CTIME = "ctime";
}