package com.fzu.chenly.commentspider.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chenly
 * @create 2022-05-29 11:18
 */
@Data
public class Member {
    /**
     * 用户id
     */
    private Long mid;
    /**
     * 昵称
     */
    private String uname;
    /**
     * 性别 男/女
     */
    private String sex;

    /**
     * 是否高级用户 0/1 6级用户为1
     */
    @JsonProperty("is_senior_member")
    private int isSeniorMember;
    /**
     * 等级信息
     */
    @JsonProperty("level_info")
    private LevelInfo levelInfo;
}
