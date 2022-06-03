package com.fzu.chenly.commentspider.exception;

import lombok.Data;

/**
 * @author chenly
 * @create 2022-05-29 13:20
 */
@Data
public class BusinessException extends RuntimeException{

    private int code;

    private String errorMessage;

    public BusinessException(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
