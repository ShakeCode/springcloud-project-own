package com.test.springcloud;

import com.test.springcloud.service.MsgSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * The type Application.当前使用eureka的server
 * 服务注册管理页面: http://127.0.0.1:8091/
 */
// 开启Zuul
@EnableZuulProxy
@EnableBinding(value = {Processor.class})
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        // 注册处理函数
        LOGGER.info("注册结果: {}", setHander(context));
        // 发送消息
        LOGGER.info("发送结果: {}", write(context));

        Assert.isNull("a", "aaa");
    }

    /**
     * Write boolean. 发送消息
     * @param context the context
     * @return the boolean
     */
    public static boolean write(ConfigurableApplicationContext context) {
        MsgSendService msgSendService = context.getBean(MsgSendService.class);
        return msgSendService.write("狗子,在吗?");
    }

    /**
     * Sets hander.   注册接收到消息时的处理函数
     * @param context the context
     * @return the hander
     */
    public static boolean setHander(ConfigurableApplicationContext context) {
        MsgSendService msgSendService = context.getBean(MsgSendService.class);
        return msgSendService.subscribe(result -> {
            LOGGER.info("狗子收到消息: {}", result.getPayload());
        });
    }
}