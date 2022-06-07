package com.springcloud.data.util.aes;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * The type Aes encryption.
 */
public class AESEncryption {

    /**
     * The Plain text.
     */
    static String plainText = "This is a plain text which need to be encrypted by AES Algorithm with CBC Mode";

    /**
     * The entry point of application.
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        // Generate Key
        SecretKey key = keyGenerator.generateKey();
        // Generating IV.
        byte[] IV = getIvBytes();

        System.out.println("IV:" + new String("").getBytes());

        System.out.println("Original Text  : " + plainText);

        byte[] cipherText = encrypt(plainText.getBytes(), key, IV);
        System.out.println("Encrypted Text : " + Base64.getEncoder().encodeToString(cipherText));

        String decryptedText = decrypt(cipherText, key, IV);
        System.out.println("DeCrypted Text : " + decryptedText);

    }

    private static byte[] getIvBytes() {
        byte[] IV = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);
        return IV;
    }

    /**
     * Encrypt byte [ ].
     * @param plaintext the plaintext
     * @param key       the key
     * @param IV        the iv
     * @return the byte [ ]
     * @throws Exception the exception
     */
    public static byte[] encrypt(byte[] plaintext, SecretKey key, byte[] IV) throws Exception {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);

        return cipherText;
    }

    /**
     * Decrypt string.
     * @param cipherText the cipher text
     * @param key        the key
     * @param IV         the iv
     * @return the string
     * @throws Exception the exception
     */
    public static String decrypt(byte[] cipherText, SecretKey key, byte[] IV) throws Exception {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        //Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText, StandardCharsets.UTF_8);
    }
}
