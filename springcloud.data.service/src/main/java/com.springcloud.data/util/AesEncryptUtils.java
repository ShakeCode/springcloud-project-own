package com.springcloud.data.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 *
 * https://blog.csdn.net/baidu_38990811/category_8262527.html
 *
 * 为了提高安全性采用了RSA,但是为了解决RSA加解密性能问题,所以采用了RSA(非对称)+AES(对称加密)方式
 *
 * 大致思路:
 *
 * 客户端启动，发送请求到服务端，服务端用RSA算法生成一对公钥和私钥，我们简称为pubkey1,prikey1，将公钥pubkey1返回给客户端。
 * 客户端拿到服务端返回的公钥pubkey1后，自己用RSA算法生成一对公钥和私钥，我们简称为pubkey2,prikey2，并将公钥pubkey2通过公钥pubkey1加密，加密之后传输给服务端。
 * 此时服务端收到客户端传输的密文，用私钥prikey1进行解密，因为数据是用公钥pubkey1加密的，通过解密就可以得到客户端生成的公钥pubkey2
 * 然后自己在生成对称加密，也就是我们的AES,其实也就是相对于我们配置中的那个16的长度的加密key,生成了这个key之后我们就用公钥pubkey2进行加密，返回给客户端，因为只有客户端有pubkey2对应的私钥prikey2，只有客户端才能解密，客户端得到数据之后，用prikey2进行解密操作，得到AES的加密key,最后就用加密key进行数据传输的加密，至此整个流程结束
 *
 * The type Aes encrypt utils.
 */
public class AesEncryptUtils {

    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     * @param content    加密的字符串
     * @param encryptKey key值
     * @return string
     * @throws Exception the exception
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        byte[] b = cipher.doFinal(content.getBytes("utf-8"));
        // 采用base64算法进行转码,避免出现中文乱码
        return Base64.encodeBase64String(b);

    }

    /**
     * 解密
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     * @return string
     * @throws Exception the exception
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    public static void main(String[] args) throws Exception {
        String userInfo = java.util.Base64.getEncoder().encodeToString("zhengxuemin".getBytes(StandardCharsets.UTF_8));
        String encryptKey = "";
        String decryptKey = "";

        System.out.println(AesEncryptUtils.encrypt(userInfo, encryptKey));
    }
}
