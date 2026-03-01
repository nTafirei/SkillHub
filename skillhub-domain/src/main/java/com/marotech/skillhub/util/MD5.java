package com.marotech.skillhub.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {

    public static String encrypt(String str) {

        Validate.isTrue(StringUtils.isNotBlank(str),
                "String to encript cannot be null or zero length");

        StringBuffer hexString = new StringBuffer();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] hash = md.digest();

            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0"
                            + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
        }

        return hexString.toString();
    }
}
