package com.springcloud.data.util.aes;

import org.springframework.security.crypto.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * The type Aes helper.
 * 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充。
 * 对称解密的目标密文为 Base64_Decode(encryptedData)。
 * 对称解密秘钥 aeskey = Base64_Decode(aesKey), aeskey 是16字节。
 * 对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
 */
public class AesHelper {

    /**
     * 微信小程序 开放数据解密
     * AES解密（Base64）
     * @param encryptedData 已加密的数据
     * @param aesKey        解密密钥
     * @param iv            IV偏移量
     * @return string string
     * @throws Exception the exception
     */
    public static String decryptForWeChatApplet(String encryptedData, String aesKey, String iv) throws Exception {
        byte[] decryptBytes = Base64.decode(encryptedData.getBytes());
        byte[] keyBytes = Base64.decode(aesKey.getBytes());
        byte[] ivBytes = Base64.decode(iv.getBytes());

        return new String(decryptByAesBytes(decryptBytes, keyBytes, ivBytes));
    }

    /**
     * AES解密
     * @param decryptedBytes 待解密的字节数组
     * @param keyBytes       解密密钥字节数组
     * @param ivBytes        IV初始化向量字节数组
     * @return byte [ ]
     * @throws Exception the exception
     */
    public static byte[] decryptByAesBytes(byte[] decryptedBytes, byte[] keyBytes, byte[] ivBytes) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] outputBytes = cipher.doFinal(decryptedBytes);;
        return outputBytes;
    }
}
