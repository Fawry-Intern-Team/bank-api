package com.example.bank_service.util;

import java.security.SecureRandom;
public class OTPUtil {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    public static String generateOTP() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return otp.toString();
    }
}
