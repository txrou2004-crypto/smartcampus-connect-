package com.smartcampus.reportinganalytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // Set hard timeouts so an offline downstream service never hangs your dashboard
        factory.setConnectTimeout(2000); // 2 seconds to connect
        factory.setReadTimeout(2000);    // 2 seconds to read data
        return new RestTemplate(factory);
    }
}
