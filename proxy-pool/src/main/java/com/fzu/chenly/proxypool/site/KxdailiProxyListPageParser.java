package com.fzu.chenly.proxypool.site;


import com.fzu.chenly.proxypool.ProxyListPageParser;
import com.fzu.chenly.proxypool.config.Constant;
import com.fzu.chenly.proxypool.domain.Proxy;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenly
 * @create 2022-05-29 16:55
 */
@Slf4j
public class KxdailiProxyListPageParser implements ProxyListPageParser {
    @Override
    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("table[class=active] tbody tr");
        List<Proxy> proxyList = new ArrayList<>();
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();

            String isAnonymous = element.select("td:eq(2)").first().text();
            String type = element.select("td:eq(3)").first().text();
            final List<String> strings = Splitter.on(",")
                    .splitToList(type);
            String newType = strings.size() == 2 ? "HTTPS" : "HTTP";
            if(!anonymousFlag || isAnonymous.contains("匿") || isAnonymous.contains("anonymous")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), newType, Constant.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
