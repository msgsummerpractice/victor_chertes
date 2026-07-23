package com.spring_example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean(name = "HelloWorld")
    public HelloWorld helloWorld() {
        HelloWorld helloWorld = new HelloWorld();

        helloWorld.setMessage("Hello World!");

        return helloWorld;
    }
}