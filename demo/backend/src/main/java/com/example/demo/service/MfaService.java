package com.example.demo.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MfaService {
    private final Map<String,String> otpStorage = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public String generateAndSendOtp(String username) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(username,otp);

        logger.info("MFA code for" + username + ": " + otp);

        return otp;
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
