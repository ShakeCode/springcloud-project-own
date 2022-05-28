package com.springcloud.data.aspect;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.springcloud.common.exception.ServiceException;
import com.springcloud.common.model.ResultVO;
import com.springcloud.data.annotation.RepeatRequestLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The type Repeat request limit aop.
 */
@Aspect
@Configuration
public class RepeatRequestLimitAop {
    private final Logger LOGGER = LoggerFactory.getLogger(RepeatRequestLimitAop.class);

    /**
     * 重复提交判断时间为8s
     */
    private final Cache<String, Integer> cache = CacheBuilder.newBuilder().expireAfterWrite(8L, TimeUnit.SECONDS).build();


    /**
     * Point cut a. 切入点
     * 1、execution()：表达式主体。
     * 2、第一个*号：表示返回类型，*号表示所有的类型。
     * 3、包名：表示需要拦截的包名，后面的两个句点分别表示当前包和当前包的所有子包，com.sample.service.impl包、子孙包下所有类的方法。
     * 4、第二个*号：表示类名，*号表示所有的类。
     * 5、*(..) ：第三个星号表示方法名，*号表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数
     */
    @Pointcut("execution(* com.springcloud.data.controller..*.*(..))")
    public void pointCutA() {

    }


    /**
     * 调用controller包下的任意类的任意方法时均会调用此方法
     * @param pjp                the pjp
     * @param repeatRequestLimit the repeat request limit
     * @return the object
     */
    @Around("execution(* com.springcloud.data.controller..*.*(..)) && @annotation(repeatRequestLimit)")
    //@Around(value=pointCut())
    public Object around(ProceedingJoinPoint pjp, RepeatRequestLimit repeatRequestLimit) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String sessionId = Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).getSessionId();
            assert attributes != null;
            HttpServletRequest request = attributes.getRequest();
            String key = sessionId + "-" + request.getServletPath();
            // 如果缓存中有这个url视为重复提交
            if (cache.getIfPresent(key) == null) {
                Object o = pjp.proceed();
                cache.put(key, 0);
                return o;
            } else {
                LOGGER.error("重复提交");
                return new ServiceException("50018", "请勿短时间内重复操作");
            }
        } catch (Throwable e) {
            LOGGER.error("验证重复提交时出现未知异常!", e);
            return ResultVO.fail(50019, "验证重复提交时出现未知异常!");
        }
    }

}
