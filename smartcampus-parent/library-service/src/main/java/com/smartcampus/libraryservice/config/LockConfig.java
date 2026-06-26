package com.smartcampus.libraryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.ReentrantLock;

@Configuration
public class LockConfig {

    @Bean
    public ReentrantLock bookLock() {
        return new ReentrantLock();
    }

    @Bean
    public ReentrantLock roomLock() {
        return new ReentrantLock();
    }
}