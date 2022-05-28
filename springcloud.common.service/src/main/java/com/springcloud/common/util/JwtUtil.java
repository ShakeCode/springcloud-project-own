package com.springcloud.common.util;

import com.alibaba.fastjson.JSON;
import com.springcloud.common.exception.ServiceException;
import com.springcloud.common.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Jwt util.
 * 官方地址: https://github.com/jwtk/jjwt
 *
 * 密钥生成：https://www.grc.com/passwords.htm#top
 * cmd下自己生成：openssl rand -base64 256
 */
@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    // HS512, HS256对称加密,Jwt的加密密钥 size >= 512 bits
    private static String secretKey = "70E86EF612B623839205EF3CA2B44607B87FBA241D9E2137EF6E34A53F33FF36";

    // 使用BASE64加密后的Jwt的加密密钥
    private static final String BASE64_SECRET_KEY = new String(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));

    //（过期时间: ms)
    private static final long ttl = 3600 * 1000;


    /**
     * 获取请求对象中的token数据
     * @param request the request
     * @return the request token
     */
    public static String getRequestToken(HttpServletRequest request) {
        String authorization = request.getHeader("gc-authorization");
        log.info("getRequestToken : {}", authorization);
        if (StringUtils.isEmpty(authorization)) {
            throw new ServiceException("error jwt");
        }
        return authorization;
    }

    /**
     * 生成JWT
     *
     *
     * JWT分成3部分：1.头部（header),2.载荷（payload, 类似于飞机上承载的物品)，3.签证（signature)
     *
     * 加密后这3部分密文的字符位数为：
     * 1.头部（header)：36位，Base64编码
     * 2.载荷（payload)：没准，BASE64编码
     * 3.签证（signature)：43位，将header和payload拼接生成一个字符串，
     * 使用HS256算法和我们提供的密钥（secret,服务器自己提供的一个字符串），
     * 对str进行加密生成最终的JWT
     * @param id       the id
     * @param subject  the subject  主题
     * @param userInfo the user info
     * @return string string
     */
    public static String createJwtToken(String id, String subject, String userInfo) {
        // 组合header
        Map<String, Object> map = new HashMap<>(2);
        map.put("alg", "HS256");
        map.put("typ", "JWT");
       /*
       //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("password", user.getPassword());*/
        // Claims claims = (Claims) Jwts.claims().put("aName", "aValue");
        JwtBuilder builder = Jwts.builder()
                .setHeaderParams(map)
                .setId(id)
                .setIssuer("spring")
                .setAudience("jwt")
                // 设置主题
                .setSubject(subject)
                // 设置jwt的签发时间
                .setIssuedAt(new Date())
                // RSASSA-PKCS-v1_5 using SHA-256,设置签名使用的签名算法和签名使用的秘钥
                //  .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                // 和解析时的密钥需要一致,位数也得一致
                .signWith(SignatureAlgorithm.HS512, BASE64_SECRET_KEY)
                .claim("user-info", userInfo);
        // JWT HMAC-SHA 算法
        // SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // String secretString = Encoders.BASE64.encode(key.getEncoded());
        // 非对称密钥
        // KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        if (ttl > 0) {
            // 设置过期时间
            builder.setExpiration(new Date(System.currentTimeMillis() + ttl));
        }
        return builder.compact();
    }

    private static PrivateKey getPrivateKey() {
        // read key
        String privateKeyB64;
        try {
            privateKeyB64 = Files.lines(Paths.get("src/main/resources/private.key")).collect(Collectors.joining());
        } catch (IOException e) {
            throw new ServiceException("read private key file error");
        }
        byte[] privateKeyDecoded = Base64.getDecoder().decode(privateKeyB64);
        // create key spec
        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(privateKeyDecoded);
        // create key form spec
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exp) {
            throw new ServiceException("generate private key error", exp);
        }
    }

    /**
     * 解析JWT
     * @param jwtStr the jwt str
     * @return claims claims
     */
    public static Claims parseJwtToken(String jwtStr) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(BASE64_SECRET_KEY)
                    .build()
                    .parseClaimsJws(jwtStr)
                    .getBody();
        } catch (ExpiredJwtException exp) {
            log.info("token is expired", exp.getMessage());
            throw exp;
        } catch (UnsupportedJwtException | MalformedJwtException |
                SignatureException exp) {
            throw new ServiceException("parse token error", exp);
        }
    }

    /**
     * Refresh token string.
     * @param token               the token
     * @param expirationInSeconds the expiration in seconds
     * @return the string
     */
    public static String refreshToken(String token, long expirationInSeconds) {
        Claims claims = parseJwtToken(token);
        Date now = new Date();
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(now.getTime() + expirationInSeconds));
        return createTokenFromClaims(claims);
    }

    private static String createTokenFromClaims(Claims claims) {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, BASE64_SECRET_KEY).compact();
    }

    /**
     * Is token expired boolean.
     * @param token the token
     * @return the boolean
     */
    public static boolean isTokenExpired(String token) {
        Claims claims;
        try {
            claims = parseJwtToken(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
        Date now = new Date();
        return now.after(claims.getExpiration());
    }

    /**
     * Validate token boolean.
     * @param token the token
     * @param user  the user
     * @return the boolean
     */
    public static boolean validateToken(String token, User user) {
        String username = getUserNameFromToken(token);
        return (username.equals(user.getUserName()) && !isTokenExpired(token));
    }

    /**
     * Gets user name from token.
     * @param token the token
     * @return the user name from token
     */
    public static String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseJwtToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 获取随机位数的字符串
     * @param length 随机位数
     * @return the random string
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // 获取ascii码中的字符 数字48-57 小写65-90 大写97-122
            int range = random.nextInt(75) + 48;
            range = range < 97 ? (range < 65 ? (range > 57 ? 114 - range : range) : (range > 90 ? 180 - range : range)) : range;
            sb.append((char) range);
        }
        return sb.toString();
    }

    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        User user = new User("dreamLi", UUID.randomUUID().toString());
        String token = JwtUtil.createJwtToken(getRandomString(36), user.getUserName(), JSON.toJSONString(user));
        log.info("get token: {}", token);
        log.info("isTokenExpired:{}", JwtUtil.isTokenExpired(token));
        Claims claims = JwtUtil.parseJwtToken(token);
        // 打印格式: {"jti":"VxflL4YAw4ZQ6D039frYTHegPzIQR65NalgE","iss":"spring","aud":"jwt","sub":"dreamLi","iat":1646491618,"user-info":"{\"userId\":\"9f7450ea-150a-41a7-a1b0-0a179bf52823\",\"userName\":\"dreamLi\"}","exp":1646495218}
        log.info("parse token 2 claims:{}", JSON.toJSONString(claims));
        log.info("validateToken:{}", JwtUtil.validateToken(token, user));
        log.info("getUserNameFromToken:{}", JwtUtil.getUserNameFromToken(token));
        String refreshToken = JwtUtil.refreshToken(token, 1000 * 1);
        log.info("refreshToken:{}", refreshToken);
        // 打印刷新token: {"jti":"knb7YC0eVMKEQPiaIkqd769T3qILNIwcDWEM","iss":"spring","aud":"jwt","sub":"dreamLi","iat":1646491949,"user-info":"{\"userId\":\"db42298a-04a9-42b7-b3a4-bb761f82fbeb\",\"userName\":\"dreamLi\"}","exp":1646491950}
        log.info("parse refreshToken:{}", JSON.toJSONString(JwtUtil.parseJwtToken(refreshToken)));

        // 生成base64二进制密钥   | cmd下自己生成：openssl rand -base64 256
        String key = "random_secret_key";
        String base64Key = DatatypeConverter.printBase64Binary(key.getBytes());
        log.info("生成base64二进制密钥:{}", base64Key);
        log.info("解密base64二进制密钥:{}", new String(DatatypeConverter.parseBase64Binary(base64Key)));
        log.info("解密base64二进制密钥:{}", new String(Base64.getDecoder().decode(base64Key)));

    }
}
