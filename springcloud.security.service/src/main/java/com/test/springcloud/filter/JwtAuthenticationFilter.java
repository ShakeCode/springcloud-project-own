package com.test.springcloud.filter;

import com.test.springcloud.service.VerifyRsaTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Jwt authentication filter. 拦截请求验证用户token过滤器, 走BasicAuthenticationFilter
 * 拦截所有需要JWT的请求，然后调用TokenAuthenticationService类的静态方法去做JWT验证
 */
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final VerifyRsaTokenService verifyRsaTokenService;

    /**
     * Instantiates a new Jwt authentication filter.
     * @param verifyRsaTokenService the verify rsa token service
     */
    public JwtAuthenticationFilter(VerifyRsaTokenService verifyRsaTokenService) {
        this.verifyRsaTokenService = verifyRsaTokenService;
    }

    /**
     * Do filter.
     * @param request     the request
     * @param response    the response
     * @param filterChain the filter chain
     * @throws IOException      the io exception
     * @throws ServletException the servlet exception
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        // 验证token有效性
        // 校验对称加密token
        // Authentication authentication = TokenAuthenticationService.verifyAuthentication((HttpServletRequest) request);
        // 校验RSA非对称加密token
        Authentication authentication = verifyRsaTokenService.verifyRsaToken((HttpServletRequest) request,
                (HttpServletResponse) response, filterChain);
        // 设置安全上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
