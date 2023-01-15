package org.example.util;

import java.nio.charset.Charset;
import java.util.Random;

public class UtilClass {
    private static final int length = 10;

    public static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (characters.length() * Math.random());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }
}
