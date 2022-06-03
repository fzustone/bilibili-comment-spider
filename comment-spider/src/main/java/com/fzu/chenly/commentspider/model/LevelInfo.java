package com.fzu.chenly.commentspider.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chenly
 * @create 2022-05-29 11:20
 */
@Data
public class LevelInfo {
    @JsonProperty("current_level")
    private int currentLevel;
}
