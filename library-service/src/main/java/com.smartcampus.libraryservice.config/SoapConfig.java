package com.smartcampus.libraryservice.config;

import com.smartcampus.libraryservice.soap.LibrarySoapService;
import jakarta.xml.ws.Endpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapConfig {

    @Bean
    public Endpoint librarySoapEndpoint(LibrarySoapService librarySoapService) {
        return Endpoint.publish("http://localhost:8084/ws/library", librarySoapService);
    }
}
