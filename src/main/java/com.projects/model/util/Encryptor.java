package com.projects.model.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
    public static final String SHA256 = "SHA-256";
    public static final String SHA1 = "SHA-1";
    public static final String MD5 = "MD-5";
    private static Logger logger = LoggerFactory.getLogger(Encryptor.class);

    public static String encrypt(String text, String algorithm) {
        if (text == null || "".equals(text)) return null;

        MessageDigest digest;
        byte[] digestBytes = null;
        try {
            digest = MessageDigest.getInstance(algorithm);

            digest.reset();
            digest.update(text.getBytes());

            digestBytes = digest.digest();

        } catch (NoSuchAlgorithmException e) {
            logger.error("failed to encrypt", e);
        }

        if (digestBytes == null) {
            return text;
        }
        StringBuilder sb = new StringBuilder();
        for (byte digestByte : digestBytes) {
            sb.append(Integer.toString((digestByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
