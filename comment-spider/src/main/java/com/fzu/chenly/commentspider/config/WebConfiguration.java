package com.fzu.chenly.commentspider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenly
 * @create 2022-05-29 12:05
 */
@Configuration
public class WebConfiguration {

    @Bean
    public RestTemplate nonProxyRestTemplate() {
        return new RestTemplate();
    }
}
