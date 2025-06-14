package com.spring.gubi.util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AES256 {

	private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding"; // AES 256 GCM 모드 (추천)
    private static final int GCM_TAG_LENGTH = 128; // GCM 인증 태그 길이 (128비트)
    private static final int IV_LENGTH = 12; // GCM 모드 권장 12바이트 IV

    private SecretKeySpec secretKeySpec;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${aes.secret}")
    private String secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        if (keyBytes.length != 32) {
            throw new IllegalArgumentException("AES 256 비밀키는 32바이트여야 합니다. 현재 길이: " + keyBytes.length);
        }

        secretKeySpec = new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }

    // 암호화
    public String encrypt(String plainText) throws Exception {
        byte[] iv = new byte[IV_LENGTH];
        secureRandom.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // iv + 암호문을 Base64 인코딩하여 반환 (복호화 시 iv 필요)
        byte[] encryptedIvAndText = new byte[IV_LENGTH + encrypted.length];
        System.arraycopy(iv, 0, encryptedIvAndText, 0, IV_LENGTH);
        System.arraycopy(encrypted, 0, encryptedIvAndText, IV_LENGTH, encrypted.length);

        return Base64.getEncoder().encodeToString(encryptedIvAndText);
    }

    // 복호화
    public String decrypt(String cipherText) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(cipherText);

        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(decoded, 0, iv, 0, IV_LENGTH);

        int encryptedSize = decoded.length - IV_LENGTH;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(decoded, IV_LENGTH, encryptedBytes, 0, encryptedSize);

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, parameterSpec);

        byte[] decrypted = cipher.doFinal(encryptedBytes);

        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
