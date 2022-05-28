package com.springcloud.common.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * The type Conversion class util.Java中的密钥和字符串转换
 * https://www.baeldung.com/java-secret-key-to-string
 *
 * 密钥是用于加密和解密消息的信息或参数。在 Java 中，我们有SecretKey一个将其定义为秘密（对称）密钥的接口。此接口的目的是对所有密钥接口进行分组（并为其提供类型安全）。
 *
 * 在 Java 中生成密钥有两种方法：从随机数生成或从给定密码派生。
 */
public class ConversionClassUtil {

    /**
     * Generate key secret key. 为了生成密钥，我们可以使用 KeyGenerator类。让我们定义一个生成SecretKey的方法——参数n指定密钥的长度（128、192 或 256），以位为单位：
     * @param n the n
     * @return the secret key
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey originalKey = keyGenerator.generateKey();
        return originalKey;
    }

    /**
     * Gets key from password.  密钥是使用基于密码的密钥导出函数（如 PBKDF2）从给定的密码导出的。我们还需要一个盐值来将密码转换为密钥。盐也是一个随机值。
     * @param password the password
     * @param salt     the salt
     * @return the key from password
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws InvalidKeySpecException  the invalid key spec exception
     */
    public static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey originalKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return originalKey;
    }

    /**
     * SecretKey转换为字节数组,使用Base64编码将字节数组转换为字符串
     * Convert secret key to string string.
     * @param secretKey the secret key
     * @return the string
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static String convertSecretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
        byte[] rawData = secretKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }

    /**
     * 字符串到SecretKey
     * Base64 解码将编码的字符串键转换为字节数组。然后，使用SecretKeySpecs，我们将字节数组转换为SecretKey：
     * Convert string to secret keyto secret key.
     * @param encodedKey the encoded key
     * @return the secret key
     */
    public static SecretKey convertStringToSecretKeyto(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return originalKey;
    }

    /**
     * The entry point of application.
     * @param args the input arguments
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws InvalidKeySpecException  the invalid key spec exception
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKey encodedKey = ConversionClassUtil.getKeyFromPassword("Baeldung@2021", "@$#baelDunG@#^$*");
        String encodedString = ConversionClassUtil.convertSecretKeyToString(encodedKey);
        System.out.println(encodedString);
        SecretKey decodeKey = ConversionClassUtil.convertStringToSecretKeyto(encodedString);
        System.out.println(encodedKey.equals(decodeKey));
    }

}
