package com.spring_example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.spring_example.model.HelloWorld;

@Configuration
@ComponentScan(basePackages = "com.spring_example")
public class AppConfig {
    @Bean
    public HelloWorld helloWorld() {
        return new HelloWorld();
    }

}
