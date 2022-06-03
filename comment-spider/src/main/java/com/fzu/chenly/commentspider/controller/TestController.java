package com.fzu.chenly.commentspider.controller;

import com.fzu.chenly.commentspider.model.BilibiliCommentResp;
import com.fzu.chenly.proxypool.config.Constant;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;

/**
 * @author chenly
 * @create 2022-05-29 19:21
 */
@RestController
public class TestController {
    private final RestTemplate restTemplate;

    public TestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/test")
    public BilibiliCommentResp get() {
        testProxyIp();
        return null;
    }

    void testProxyIp() {

        String url = "http://www.httpbin.org/ip";

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("122.9.101.6", 8888)  //设置代理服务
        ));
        restTemplate.setRequestFactory(requestFactory);

        HttpHeaders headers = new HttpHeaders();
        //headers.set(HttpHeaders.USER_AGENT, Constant.getUserAgent());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<String> response = null;
        try {
            response = restTemplate.exchange("http://baidu.com/", HttpMethod.GET, entity, String.class);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
        //发送请求
        String result = restTemplate.getForObject(url, String.class);
        //打印响应结果
        System.out.println(result);
    }
}
