package com.springcloud.data.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口重复提交：是由于网络等原因，造成一瞬间发送多个请求，造成insert，update操作，多次数据被修改。如果多个请求时间间隔足够的小，那么可以理解为并发问题；即并发情况下，只有一次操作成功
 *
 * 解决方案：
 * 1. 拦截器、过滤器+切面
 * 2. 前端携带特定时间期限(可限制X秒)的key请求后台,后台对参数MD5加密,IP,URL情况判断是否相同判断是不是前端多次发起一样的请求
 * 3. 加锁判断，分布式使用分布式锁, 单体服务使用内存锁
 * 4. 缓存技术: 单体服务使用JVM获本地磁盘类缓存,分布式可以使用redis等
 *
 * The interface Repeat request limit. 限制重复提交注解
 */
@Inherited
@Documented
@Target(ElementType.METHOD
)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatRequestLimit {

}
