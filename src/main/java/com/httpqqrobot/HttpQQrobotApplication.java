package com.httpqqrobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HttpQQrobotApplication {
    public static void main(String[] args) {
        SpringApplication.run(HttpQQrobotApplication.class, args);
    }
}