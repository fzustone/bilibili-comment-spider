package com.fzu.chenly.commentspider.model.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * ${DESCRIPTION} 
 * @author chenly
 * @create 2022-05-29 20:36
 */

@Data
@TableName(value = "proxy_ip")
public class ProxyIp {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ip")
    private String ip;

    @TableField(value = "port")
    private Integer port;

    @TableField(value = "`type`")
    private String type;

    public static final String COL_ID = "id";

    public static final String COL_IP = "ip";

    public static final String COL_PORT = "port";

    public static final String COL_TYPE = "type";
}