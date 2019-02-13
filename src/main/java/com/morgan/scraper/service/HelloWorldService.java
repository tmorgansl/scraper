package com.morgan.scraper.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class HelloWorldService {
    @Value("${name:World}")
    private String name;

    @Value("${duration:10s}")
    private Duration duration;

    public String getHelloMessage() {
        return "Hello " + this.name + " for " + this.duration.getSeconds() + " seconds";
    }
}
