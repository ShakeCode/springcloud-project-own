package com.springcloud.data.filter;

import com.springcloud.data.model.HeaderInfo;
import com.springcloud.data.util.HeaderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * The type Login filter.
 */
@Component
public class LoginFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(LoginFilter.class);

    /**
     * Init.
     * @param filterConfig the filter config
     * @throws ServletException the servlet exception
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Do filter.
     * @param servletRequest  the servlet request
     * @param servletResponse the servlet response
     * @param filterChain     the filter chain
     * @throws IOException      the io exception
     * @throws ServletException the servlet exception
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 打印请求信息
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("------------- LoginFilter start -------------");
        LOG.info("请求地址:{},{}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("远程地址:{}", request.getRemoteAddr());
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        // 设置主子线程共用的上下文信息
        HeaderContext.setHeaderInfo(
                HeaderInfo.builder()
                        .userInfo(request.getHeader("user-info"))
                        .gcAuthentication(request.getHeader("gc-authentication"))
                        .lang(request.getHeader("lang"))
                        .tenantCode(request.getHeader("tenant-code"))
                        .build());
        LOG.info("------------- LoginFilter end  waste time:{} ms -------------", System.currentTimeMillis() - startTime);
    }

    /**
     * 在销毁Filter时自动调用。
     */
    @Override
    public void destroy() {
        System.out.println("destory login-filter");
    }
}
