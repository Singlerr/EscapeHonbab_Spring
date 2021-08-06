package io.github.escapehonbab.controller.service;

import io.github.escapehonbab.lang.StaticMessage;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class VerificationService {
    private static final HashMap<String, String> unverifiedPhoneNumbers = new HashMap<>();

    public static void add(String phoneNumber, String verificationCode) {
        unverifiedPhoneNumbers.put(phoneNumber, verificationCode);
    }

    public static void remove(String phoneNumber) {
        if (exists(phoneNumber))
            unverifiedPhoneNumbers.remove(phoneNumber);
    }

    public static boolean exists(String phoneNumber) {
        return unverifiedPhoneNumbers.containsKey(phoneNumber);
    }

    public static boolean codeExists(String code) {
        return unverifiedPhoneNumbers.containsValue(code);
    }

    public static String addNewVerification(String phoneNumber) {
        String code = "00000";
        do {
            code = generateCode(5);
        } while (codeExists(code));
        add(phoneNumber, code);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (exists(phoneNumber))
                    remove(phoneNumber);
            }
        }, 110 * 1000);
        return code;
    }

    public static boolean checkVerification(String phoneNumber, String code) {
        return exists(phoneNumber) && codeExists(phoneNumber) && unverifiedPhoneNumbers.get(phoneNumber).equals(code);
    }

    public static String getVerificationCheckMessage(String phoneNumber, String code) {
        if (!exists(phoneNumber) || !codeExists(code))
            return StaticMessage.Verification.VERIFICATION_NOT_FOUND;
        if (!unverifiedPhoneNumbers.get(phoneNumber).equalsIgnoreCase(code))
            return StaticMessage.Verification.CODE_MISMATCH;

        return StaticMessage.Verification.SUCCESS_VERIFICATION;
    }

    public static String generateCode(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
