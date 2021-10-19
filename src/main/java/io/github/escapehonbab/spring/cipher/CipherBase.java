package io.github.escapehonbab.spring.cipher;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CipherBase {
    private static CipherBase instance;
    private final String PRIVATE_KEY = "Vaf6vj6MmVo1NIUbKfk1SfXx3JGdM48B";

    private CipherBase() {
    }

    public static CipherBase getInstance() {
        if (instance == null)
            return (instance = new CipherBase());
        return instance;
    }

    public String encode(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(str.getBytes(StandardCharsets.UTF_8));
        byte[] data = digest.digest();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++)
            builder.append(Integer.toString(data[i] & 0xff + 0x100, 16)).substring(1);

        return builder.toString();
    }

    public byte[] encode(InputStream inputStream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] data = inputStream.readAllBytes();
        SecretKey secretKey = new SecretKeySpec(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(PRIVATE_KEY.substring(0, 16).getBytes(StandardCharsets.UTF_8)));

        return cipher.doFinal(data);
    }

    public byte[] encode(byte[] data) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secretKey = new SecretKeySpec(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(PRIVATE_KEY.substring(0, 16).getBytes(StandardCharsets.UTF_8)));

        return cipher.doFinal(data);
    }

    public InputStream decode(InputStream inputStream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secretKey = new SecretKeySpec(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(PRIVATE_KEY.substring(0, 16).getBytes(StandardCharsets.UTF_8)));
        return new CipherInputStream(inputStream, cipher);
    }
}
