package com.fzu.chenly.proxypool;



import com.fzu.chenly.proxypool.domain.Proxy;

import java.util.List;

/**
 * Created by tony on 2017/10/19.
 */
public interface ProxyListPageParser {

    /**
     * 是否只要匿名代理
     */
    boolean anonymousFlag = true;
    List<Proxy> parse(String content);
}
