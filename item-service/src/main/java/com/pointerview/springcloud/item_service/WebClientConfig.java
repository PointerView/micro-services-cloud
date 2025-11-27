package com.pointerview.springcloud.item_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${config.base.endpoint.msvc-products}")
    private String url;

    @Bean
    @LoadBalanced
    public WebClient.Builder webClient() {
        return WebClient.builder().baseUrl(url);
    }
}
