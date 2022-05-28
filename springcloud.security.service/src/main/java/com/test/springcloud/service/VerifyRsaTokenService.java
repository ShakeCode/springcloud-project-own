package com.test.springcloud.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.common.exception.ServiceException;
import com.test.springcloud.config.RsaKeyProperties;
import com.test.springcloud.constant.Constant;
import com.test.springcloud.model.Payload;
import com.test.springcloud.model.UserPojo;
import com.test.springcloud.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Verify rsa token service.
 */
@Service
public class VerifyRsaTokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyRsaTokenService.class);

    private final RsaKeyProperties prop;

    /**
     * Instantiates a new Verify rsa token service.
     * @param prop the prop
     */
    public VerifyRsaTokenService(RsaKeyProperties prop) {
        this.prop = prop;
    }

    /**
     * Verify rsa token authentication.
     * @param request  the request
     * @param response the response
     * @param chain    the chain
     * @return the authentication
     * @throws IOException      the io exception
     * @throws ServletException the servlet exception
     */
    public Authentication verifyRsaToken(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("verifyRsaToken...");
        String token = request.getHeader(Constant.GC_AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            chain.doFilter(request, response);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            PrintWriter out = response.getWriter();
            Map<String, Object> resultMap = new HashMap<>(2);
            resultMap.put("code", HttpServletResponse.SC_FORBIDDEN);
            resultMap.put("msg", "请登录！");
            out.write(new ObjectMapper().writeValueAsString(resultMap));
            out.flush();
            out.close();
        } else {
            // 验证token是否正确
            Payload<UserPojo> payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), UserPojo.class);
            UserPojo user = payload.getUserInfo();
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
            }
        }
        return null;
    }

    /**
     * Verify rsa token authentication.
     * @param request the request
     * @return the authentication
     * @throws IOException      the io exception
     * @throws ServletException the servlet exception
     */
    public Authentication verifyRsaToken(HttpServletRequest request) {
        String token = request.getHeader(Constant.GC_AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            throw new ServiceException("获取token失败");
        }
        // 验证token是否正确
        Payload<UserPojo> payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), UserPojo.class);
        UserPojo user = payload.getUserInfo();
        if (user != null) {
            return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
        }
        throw new ServiceException("获取token user失败");
    }
}
