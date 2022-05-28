package com.test.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;


/**
 * The type Application.
 *
 * 网关的作用:
 *
 * 网关对所有服务会话进行拦截
 * 网关安全控制、统一异常处理、xxs、sql注入
 * 权限控制、黑名单和白名单、性能监控、日志打印等
 * https://blog.csdn.net/qq_38252039/article/details/90116113
 */
@EnableZuulServer
// 开启Zuul
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {
    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}