package com.test.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.util.Arrays;


/**
 * The type Application. http://localhost:8060/login
 * 默认用户名： user
 * 启动服务后控制台打印密码 (随机UUID())
 *
 * 登录后跳转百度
 * http://localhost:8060/oauth/authorize?client_id=client&response_type=code&redirect_uri=http://www.baidu.com
 */
@EnableWebSecurity
@EnableAuthorizationServer
@EnableDiscoveryClient
@SpringBootApplication
public class Application {
    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private final AuthenticationProvider authenticationProvider;

    /**
     * Instantiates a new Application.
     * @param authenticationProvider the authentication provider
     */
    public Application(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Authentication manager authentication manager.
     * @return the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(authenticationProvider));
    }
}