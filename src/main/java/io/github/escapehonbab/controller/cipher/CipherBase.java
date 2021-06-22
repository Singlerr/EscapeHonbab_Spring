package io.github.escapehonbab.controller.cipher;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CipherBase {
    private final String PRIVATE_KEY = "Vaf6vj6MmVo1NIUbKfk1SfXx3JGdM48B";

    private static CipherBase instance;

    private CipherBase(){}

    public static CipherBase getInstance(){
        if(instance == null)
            return (instance = new CipherBase());
        return instance;
    }
    public byte[] encode(InputStream inputStream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] data = inputStream.readAllBytes();

        SecretKey secretKey = new SecretKeySpec(data,"AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,new IvParameterSpec(PRIVATE_KEY.substring(0,16).getBytes(StandardCharsets.UTF_8)));

        byte[] encoded = Base64.getDecoder().decode(data);

        return cipher.doFinal(encoded);
    }
    public byte[] encode(byte[] data) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secretKey = new SecretKeySpec(data,"AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,new IvParameterSpec(PRIVATE_KEY.substring(0,16).getBytes(StandardCharsets.UTF_8)));

        byte[] encoded = Base64.getDecoder().decode(data);

        return cipher.doFinal(encoded);
    }

    public InputStream decode(InputStream inputStream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secretKey = new SecretKeySpec(inputStream.readAllBytes(),"AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,secretKey,new IvParameterSpec(PRIVATE_KEY.substring(0,16).getBytes(StandardCharsets.UTF_8)));
        return new CipherInputStream(inputStream,cipher);
    }
}
