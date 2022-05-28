package com.test.springcloud.controller;

import com.test.springcloud.util.RsaUtils;
import org.junit.Test;

public class JwtTest {
    private String privateKey = "F:\\ideaWorkplace\\springcloud.service.demo\\springcloud.security.service\\src\\main\\resources\\auth_key\\id_key_rsa";

    private String publicKey = "F:\\ideaWorkplace\\springcloud.service.demo\\springcloud.security.service\\src\\main\\resources\\auth_key\\id_key_rsa.pub";

    @Test
    public void test1() throws Exception {
        RsaUtils.generateKey(publicKey, privateKey, "jwt", 1024);
    }
}
