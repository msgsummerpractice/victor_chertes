package com.example.demo.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

@Service
public class MfaService {
    private final Map<String,String> otpStorage = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String email;

    public MfaService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String generateAndSendOtp(String username) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(username,otp);

        sendMfaEmail(username, otp);

        logger.info("MFA code for" + username + ": " + otp);

        return otp;
    }

    private void sendMfaEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("victor.chertes.vc@gmail.com"); 
            message.setTo(email);
            message.setSubject("Your Authentication Code");
            message.setText("Hello,\n\nYour Two-Factor Authentication (MFA) code is: " + otp + 
                            "\n\nPlease use this code to complete your login. It is valid for a limited time.\n\nThank you!");
            
            mailSender.send(message);
            logger.info("Email successfully sent to {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send MFA email to {}: {}", toEmail, e.getMessage());
        }
    }

    public boolean verifyOtp(String username,String code) {
        String storedOtp = otpStorage.get(username);

        if (storedOtp != null && storedOtp.equals(code)) {
            otpStorage.remove(username); 
            return true;
        }
        return false;
    }
}
