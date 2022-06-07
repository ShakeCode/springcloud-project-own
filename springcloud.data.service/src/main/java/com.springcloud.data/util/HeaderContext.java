package com.springcloud.data.util;

import com.springcloud.data.model.HeaderInfo;

/**
 * The type Header context.
 *
 * ThreadLocal 在线程池情况下慎用，线程的复用导致线程的内存数据传递不正确，
 *
 * 可使用:
 * 1.spring的TaskDecorator来装饰线程（推荐）
 * 2. 使用阿里线程工具类   https://github.com/alibaba/transmittable-thread-local
 * <dependency>
 *   <groupId>com.alibaba</groupId>
 *   <artifactId>transmittable-thread-local</artifactId>
 * </dependency>
 *
 * 关于InheritableThreadLocal的问题
 *
 * https://blog.csdn.net/lmx1989219/article/details/107598202
 *
 * https://www.jianshu.com/p/4093add7f2cd
 *
 * https://zhuanlan.zhihu.com/p/113388946
 */
public class HeaderContext {
    private static final ThreadLocal<HeaderInfo> THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * Gets header info.
     * @return the header info
     */
    public static HeaderInfo getHeaderInfo() {
        return THREAD_LOCAL.get();
    }

    /**
     * Sets header info.
     * @param headerInfo the header info
     */
    public static void setHeaderInfo(HeaderInfo headerInfo) {
        THREAD_LOCAL.set(headerInfo);
    }

    /**
     * Clear.
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }

}
