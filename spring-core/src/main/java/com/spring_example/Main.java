package com.spring_example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.spring_example.client.NotificationClient;
import com.spring_example.config.AppConfig;

public class Main {
  
   public static void main(String[] args) {
     
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      
      NotificationClient manager = context.getBean(NotificationClient.class);
      
      manager.sendNotification("Hello, this is a test notification!");
     
   }
}