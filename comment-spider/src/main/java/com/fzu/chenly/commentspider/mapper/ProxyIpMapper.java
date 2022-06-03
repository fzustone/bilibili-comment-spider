package com.fzu.chenly.commentspider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fzu.chenly.commentspider.model.dao.ProxyIp;
import org.springframework.stereotype.Repository;

/**
 * ${DESCRIPTION} 
 * @author chenly
 * @create 2022-05-29 20:36
 */
@Repository
public interface ProxyIpMapper extends BaseMapper<ProxyIp> {
    void truncate();
}