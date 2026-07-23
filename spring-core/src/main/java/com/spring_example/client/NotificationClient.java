package com.spring_example.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.spring_example.service.MessageService;

@Component
public class NotificationClient {
    private final MessageService messageService;

    @Autowired
    public NotificationClient(@Qualifier("smsService") MessageService messageService) {
        this.messageService = messageService;
    }

    public void sendNotification(String message) {
        messageService.sendMessage(message);
    }
}