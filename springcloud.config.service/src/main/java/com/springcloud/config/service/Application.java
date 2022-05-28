package com.springcloud.config.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * The type com.springcloud.config.service.Application. 配置中心服务端
 *
 * 本地访问(不需要带上服务上下文名称):
 * 格式：  http://127.0.0.1:8099/定义的服务名/文件profile名称
 * http://127.0.0.1:8099/springcloud-config-server/base
 *
 *
 * 当微服务过多的时候,每个微服务的配置很难集中管理。SpringCloud Config通过git/svn代码托管来实现配置的集中管理。实现配置中心客户端获取远程的配置文件，并可以动态刷新，即时生效。
 */
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class Application {
    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}