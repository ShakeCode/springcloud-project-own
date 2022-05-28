package com.springcloud.data.interceptor;

import com.springcloud.data.annotation.RepeatRequestLimit;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * The type Repeat request interceptor.
 */
@Configuration
public abstract class RepeatRequestInterceptor implements HandlerInterceptor {
    /**
     * Pre handle boolean.
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @return the boolean
     * @throws Exception the exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Optional<RepeatRequestLimit> annotation = Optional.ofNullable(method.getAnnotation(RepeatRequestLimit.class));
            if (annotation.isPresent()) {
                if (this.isRepeatSubmit(request)) {
                    // 返回客户端  不允许重复提交
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Post handle.
     * @param request      the request
     * @param response     the response
     * @param handler      the handler
     * @param modelAndView the model and view
     * @throws Exception the exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * After completion.
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @param ex       the ex
     * @throws Exception the exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     * @param request the request
     * @return boolean
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request);
}
