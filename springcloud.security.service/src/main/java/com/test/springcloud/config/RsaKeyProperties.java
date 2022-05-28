package com.test.springcloud.config;

import com.test.springcloud.util.RsaUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * The type Rsa key properties.
 */
@Data
@Component
@ConfigurationProperties(prefix = "rsa.key")
public class RsaKeyProperties {
    @ApiModelProperty("公钥文件")
    private String pubKeyFile;

    @ApiModelProperty("私钥文件")
    private String priKeyFile;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    /**
     * 系统启动的时候触发
     * @throws Exception the exception
     */
    @PostConstruct
    public void createRsaKey() throws Exception {
        publicKey = RsaUtils.getPublicKey(pubKeyFile);
        privateKey = RsaUtils.getPrivateKey(priKeyFile);
    }
}
