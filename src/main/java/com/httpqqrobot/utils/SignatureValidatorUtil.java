package com.httpqqrobot.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignatureValidatorUtil {
    public static boolean validateSignature(String token, String timestamp, String nonce, String signature) {
        // 1. 将token、timestamp、nonce进行字典序排序
        String[] stringsForSorting = {token, timestamp, nonce};
        Arrays.sort(stringsForSorting);

        // 2. 拼接成一个字符串并进行sha1加密
        String concatenatedString = String.join("", stringsForSorting);
        String sha1Hash = getSha1(concatenatedString);

        // 3. 然后与signature对比
        return sha1Hash.equals(signature);
    }

    public static String getSha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
