package com.wangran.wechat.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @author ：Wang
 * @date ：Created in 2020/3/29 11:39
 * @description：
 */
@Configuration
public class RestTemplateConfig {

    private final String wechatRestfulUrl="http://106.52.81.158/api/";

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Bean
    public RestTemplate restTemplate(){
        return restTemplateBuilder.setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(20000))
                .rootUri(wechatRestfulUrl).build();
    }
}