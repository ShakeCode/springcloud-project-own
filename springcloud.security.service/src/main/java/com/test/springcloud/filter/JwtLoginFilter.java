package com.test.springcloud.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.common.exception.ServiceException;
import com.springcloud.common.model.ResultVO;
import com.test.springcloud.config.RsaKeyProperties;
import com.test.springcloud.constant.Constant;
import com.test.springcloud.model.AccountCredentials;
import com.test.springcloud.model.UserPojo;
import com.test.springcloud.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Jwt login filter.
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtLoginFilter.class);

    private final RsaKeyProperties prop;

    /**
     * Instantiates a new Jwt login filter.
     * @param url         the url
     * @param authManager the auth manager
     */
    public JwtLoginFilter(String url, AuthenticationManager authManager, RsaKeyProperties prop) {
        super(new AntPathRequestMatcher(url));
        this.prop = prop;
        setAuthenticationManager(authManager);
    }

    /**
     * Attempt authentication authentication.登录时需要验证时候调用, 尝试验证用户权限,返回用户Authentication信息
     * @param req the req
     * @param res the res
     * @return the authentication
     * @throws AuthenticationException the authentication exception
     * @throws IOException             the io exception
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException {
        try {
            LOGGER.info("attemptAuthentication ...");
            // JSON反序列化成 AccountCredentials
            AccountCredentials creds = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);
            // 返回一个验证令牌
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUserName(),
                            creds.getPassword()
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            try {
                res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter out = res.getWriter();
                Map<String, Object> resultMap = new HashMap<>(2);
                resultMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
                resultMap.put("msg", "用户名或密码错误");
                out.write(new ObjectMapper().writeValueAsString(resultMap));
                out.flush();
                out.close();
            } catch (Exception outEx) {
                outEx.printStackTrace();
            }
            throw new ServiceException("认证异常", e);
        }
        return null;
    }


    /**
     * Successful authentication. 验证成功返回token
     * @param req   the req
     * @param res   the res
     * @param chain the chain
     * @param auth  the auth
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain,
            Authentication auth) {
        LOGGER.info("successfulAuthentication ...");
        this.returnRsaToken(res, auth);
        // 校验通过,返回用户token, 使用对称加密HmacSHA512
        //TokenAuthenticationService.createAuthentication(res, auth.getName());
    }

    private void returnRsaToken(HttpServletResponse res, Authentication auth) {
        UserPojo user = new UserPojo();
        user.setUsername(auth.getName());
        // user.setRoles((List<RolePojo>) auth.getAuthorities());
        String token = JwtUtils.generateTokenExpireInSeconds(user, prop.getPrivateKey(), 24 * 60);
        res.addHeader(Constant.GC_AUTHORIZATION, token);
        try {
            res.setContentType("application/json");
            res.setStatus(HttpServletResponse.SC_OK);
            res.getOutputStream().println(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Unsuccessful authentication. 验证失败返回失败信息
     * @param request  the request
     * @param response the response
     * @param failed   the failed
     * @throws IOException      the io exception
     * @throws ServletException the servlet exception
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse
            response, AuthenticationException failed) throws IOException, ServletException {
        LOGGER.info("unsuccessfulAuthentication ...");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream().println(JSON.toJSONString(ResultVO.fail(500, "Internal Server Error")));
    }
}



