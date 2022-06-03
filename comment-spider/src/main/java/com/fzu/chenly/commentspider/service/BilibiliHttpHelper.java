package com.fzu.chenly.commentspider.service;

import com.fzu.chenly.commentspider.model.BilibiliCommentResp;
import com.fzu.chenly.commentspider.model.CommentData;
import com.fzu.chenly.commentspider.model.dao.ProxyIp;
import com.fzu.chenly.commentspider.util.JsonUtils;
import com.fzu.chenly.proxypool.config.Constant;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chenly
 * @create 2022-05-29 12:09
 */
@Service
@Slf4j
public class BilibiliHttpHelper {
    private static final String URL_TEMPLATE = BilibiliHttpHelper.getUrlTemplate();

    @SneakyThrows
    public CommentData fetchCommentWithProxy(ProxyIp proxy, Long oid, Integer page, RestTemplate restTemplate) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy.getIp(), proxy.getPort())));

        restTemplate.setRequestFactory(requestFactory);
        return fetchComment(oid, page, restTemplate);

    }

    /**
     * https://api.bilibili.com/x/v2/reply/main?callback=jQuery1720675916866313846_1653792588639&jsonp=jsonp&next=0&type=1&oid=299172232&mode=3&plat=1&_=1653792596575
     *
     * @param oid
     * @param page
     * @return
     */
    public CommentData fetchComment(Long oid, Integer page, RestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, Constant.getUserAgent());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        Map<String, Object> params = buildParamMap(oid, page);
        HttpEntity<BilibiliCommentResp> response = null;
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            response = restTemplate.exchange(URL_TEMPLATE, HttpMethod.GET, entity, BilibiliCommentResp.class, params);
        } catch (Exception e) {
            log.error("请求异常：", e);
            return null;
        } finally {
            log.info("请求耗时：{} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
        if (!response.hasBody()) {
            log.error("返回结果无数据");
            return null;
        }
        final BilibiliCommentResp body = response.getBody();
        assert body != null;
        if (body.getCode() != 0) {
            log.error("请求B站API结果异常:code:{}, msg:{}", body.getCode(), body.getMessage());
            return null;
        }
        return body.getData();
    }

    public static String getUrlTemplate() {
        return UriComponentsBuilder.fromHttpUrl("https://api.bilibili.com/x/v2/reply/main")
                .queryParam("jsonp", "{jsonp}")
                .queryParam("next", "{next}")
                .queryParam("type", "{type}")
                .queryParam("oid", "{oid}")
                .queryParam("mode", "{mode}")
                .queryParam("plat", "{plat}")
                .encode()
                .toUriString();
    }

    /**
     * @param oid 视频id
     * @param page 起始0
     * @return
     */
    public static Map<String, Object> buildParamMap(Long oid, Integer page) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(6);
        params.put("jsonp", "jsonp");
        params.put("next", page);
        params.put("type", "1");
        params.put("oid", oid);
        params.put("mode", "3");
        params.put("plat", "1");
        return params;
    }

}
