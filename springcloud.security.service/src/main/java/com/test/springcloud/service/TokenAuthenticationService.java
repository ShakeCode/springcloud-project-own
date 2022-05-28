package com.test.springcloud.service;

import com.springcloud.common.exception.ServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * The type Token authentication service. 使用HmacSHA512 对称加密
 */
@Component
public class TokenAuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationService.class);

    // 过期时间: 5天
    private static final long EXPIRATIONTIME = 432_000_000;

    // JWT密钥
    private static final String SECRET = "70E86EF612B623839205EF3CA2B44607B87FBA241D9E2137EF6E34A53F33FF3670E86EF612B623839205EF3CA2B44607B87FBA241D9E2137EF6E34A53F33FF36";

    // 存放Token的Header Key
    private static final String HEADER_TOKEN_KEY = "gc-authorization";

    /**
     * create authentication.JWT生成方法
     * @param response the response
     * @param username the username
     */
    public static void createAuthentication(HttpServletResponse response, String username) {
        // 生成JWT
        String jwtToken = Jwts.builder()
                // 保存权限（角色）
                .claim("authorities", "ROLE_ADMIN,AUTH_WRITE")
                // 用户名写入标题
                .setSubject(username)
                // 有效期设置
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                // 签名设置
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        try {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            // 将 JWT 写入 body
            response.getOutputStream().println(jwtToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * verify authentication. JWT验证方法
     * @param request the request
     * @return the authentication
     */
    public static Authentication verifyAuthentication(HttpServletRequest request) {
        LOGGER.info("verifyAuthentication start...");
        // 从Header中拿到token
        String token = request.getHeader(HEADER_TOKEN_KEY);
        if (token != null) {
            // 解析 Token
            Claims claims = Jwts.parserBuilder()
                    // 验签
                    .setSigningKey(SECRET)
                    .build()
                    // 去掉 Bearer
                    .parseClaimsJws(token)
                    .getBody();
            // 拿用户名
            String user = claims.getSubject();
            // 得到 权限（角色）
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
            // 返回验证令牌
            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, authorities) :
                    null;
        }
        throw new ServiceException("token验证失败");
    }
}
