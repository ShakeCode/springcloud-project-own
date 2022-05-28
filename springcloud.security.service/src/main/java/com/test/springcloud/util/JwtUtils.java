package com.test.springcloud.util;

import com.springcloud.common.exception.ServiceException;
import com.test.springcloud.config.RsaKeyProperties;
import com.test.springcloud.model.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * The type Jwt utils.
 */
@Component
public class JwtUtils {
    private static final String JWT_PAYLOAD_USER_KEY = "user-info";

    private static RsaKeyProperties prop;

   /* @PostConstruct
    public static void setProp(RsaKeyProperties prop) {
        JwtUtils.prop = prop;
    }
*/

    /**
     * 私钥加密token
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位分钟
     * @return JWT string
     */
    public static String generateTokenExpireInMinutes(Object userInfo, PrivateKey privateKey, int expire) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toString(userInfo))
                .setId(createJwtId())
                .setExpiration(DateTime.now().plusMinutes(expire).toDate())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 私钥加密token
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位秒
     * @return JWT string
     */
    public static String generateTokenExpireInSeconds(Object userInfo, PrivateKey privateKey, int expire) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toString(userInfo))
                .setId(createJwtId())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 公钥解析token
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        try {
            return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
        } catch (Exception e) {
            throw new ServiceException("解析token失敗", e);
        }
    }

    /**
     * Parser token body string.
     * @param token     the token
     * @param publicKey the public key
     * @return the string
     */
    public static String parserTokenBody(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(prop.getPublicKey()).build().parseClaimsJws(token).getBody().toString();
        } catch (Exception e) {
            throw new ServiceException("解析token失敗", e);
        }
    }

    /**
     * Is token expired boolean. 判断token是否过期
     * @param token     the token
     * @param publicKey the public key
     * @return the boolean
     */
    public static boolean isTokenExpired(String token, PublicKey publicKey) {
        Claims claims;
        try {
            claims = parserToken(token, publicKey).getBody();
        } catch (ExpiredJwtException e) {
            return true;
        }
        Date now = new Date();
        return now.after(claims.getExpiration());
    }

    private static String createJwtId() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    /**
     * 获取token中的用户信息
     * @param <T>       the type parameter
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @param userType  the user type
     * @return 用户信息 info from token
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey, Class<T> userType) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setUserInfo(JsonUtils.toBean(body.get(JWT_PAYLOAD_USER_KEY).toString(), userType));
        claims.setExpiration(body.getExpiration());
        return claims;
    }

    /**
     * 获取token中的载荷信息
     * @param <T>       the type parameter
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息 info from token
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setExpiration(body.getExpiration());
        return claims;
    }
}
