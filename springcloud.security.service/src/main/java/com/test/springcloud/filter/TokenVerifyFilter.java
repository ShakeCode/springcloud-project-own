package com.test.springcloud.filter;

import com.test.springcloud.service.VerifyRsaTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Token verify filter.
 */
@Component
public class TokenVerifyFilter extends BasicAuthenticationFilter {

    private final VerifyRsaTokenService verifyRsaTokenService;

    /**
     * Instantiates a new Token verify filter.
     * @param authenticationManager the authentication manager
     * @param verifyRsaTokenService the verify rsa token service
     */
    public TokenVerifyFilter(AuthenticationManager authenticationManager, VerifyRsaTokenService verifyRsaTokenService) {
        super(authenticationManager);
        this.verifyRsaTokenService = verifyRsaTokenService;
    }

    /**
     * Do filter internal.
     * @param request  the request
     * @param response the response
     * @param chain    the chain
     * @throws IOException      the io exception
     * @throws ServletException the servlet exception
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = verifyRsaTokenService.verifyRsaToken(request, response, chain);
        // 设置安全上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}