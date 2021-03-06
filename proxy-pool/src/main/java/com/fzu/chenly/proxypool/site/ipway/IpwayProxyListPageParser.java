package com.fzu.chenly.proxypool.site.ipway;

import com.fzu.chenly.proxypool.ProxyListPageParser;
import com.fzu.chenly.proxypool.config.Constant;
import com.fzu.chenly.proxypool.domain.Proxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class IpwayProxyListPageParser implements ProxyListPageParser {
    @Override
    public List<Proxy> parse(String content) {
        Document document = Jsoup.parse(content);
        String[] ipArr = document.select("div").get(1).html().replaceAll("\r|\n", "").split("<br>");
        List<Proxy> proxyList = new ArrayList<>(ipArr.length);
        for (int i = 1; i < ipArr.length; i++){
            String proxyStr = ipArr[i];
            if(proxyStr != null && proxyStr.contains(":")){
                int colonIndex = proxyStr.indexOf(":");
                String ip = proxyStr.substring(0, colonIndex);
                String port = proxyStr.substring(colonIndex+1, proxyStr.length());
                proxyList.add(new Proxy(ip, Integer.valueOf(port), Constant.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
