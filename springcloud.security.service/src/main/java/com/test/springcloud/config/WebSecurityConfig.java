package com.test.springcloud.config;


import com.test.springcloud.filter.JwtLoginFilter;
import com.test.springcloud.filter.TokenVerifyFilter;
import com.test.springcloud.service.VerifyRsaTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 执行过程:
 *
 * 1.先程序启动 - main函数
 *
 * 2. 注册验证组件 - WebSecurityConfig 类 configure(AuthenticationManagerBuilder auth)方法,这里我们注册了自定义验证组件
 *
 * 3. 设置验证规则 - WebSecurityConfig 类 configure(HttpSecurity http)方法，这里设置了各种路由访问规则
 *
 * 4. 初始化过滤组件 - JwtLoginFilter 和 JwtAuthenticationFilter 类会初始化
 *
 * The type O auth web config.
 */
@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final VerifyRsaTokenService verifyRsaTokenService;

    private final RsaKeyProperties prop;

    /**
     * Instantiates a new Web security config.
     * @param verifyRsaTokenService the verify rsa token service
     * @param prop                  the prop
     */
    public WebSecurityConfig(VerifyRsaTokenService verifyRsaTokenService, RsaKeyProperties prop) {
        this.verifyRsaTokenService = verifyRsaTokenService;
        this.prop = prop;
    }


    /**
     * Configure.
     * @param web the web
     * @throws Exception the exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/favor.ico");
    }

    /**
     * Configure.  配置各种路由的拦截验证策略
     * @param http the http
     * @throws Exception the exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("config web security");
        super.configure(http);
        // 关闭csrf验证
        http.csrf()
                .disable()
                // 对请求进行认证
                .authorizeRequests()
                // 所有/的所有请求 都放行
                .antMatchers("/").permitAll()
                // 所有 /login 的POST请求 都放行
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                // 权限检查
                .antMatchers("/hello").hasAuthority("AUTH_WRITE")
                // 角色检查
                .antMatchers("/world").hasRole("ADMIN")
                // 所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                // 添加一个过滤器 所有访问 /login 的请求交给 JWTLoginFilter 来处理 这个类处理所有的JWT相关内容
                // 使用对称加密生成token
                //.addFilterBefore(new JwtLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                // 添加一个过滤器验证其他请求的Token是否合法 使用对称加密
                //.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 使用非对称RSA加密
                .addFilterBefore(new JwtLoginFilter("/login", authenticationManager(), prop), UsernamePasswordAuthenticationFilter.class)
                // 使用非对称RSA加密,设置校验token过滤器
                .addFilter(new TokenVerifyFilter(super.authenticationManager(), verifyRsaTokenService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * Configure. 使用自定义身份验证组件
     * @param auth the auth
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // 使用自定义身份验证组件
        auth.authenticationProvider(new CustomSsoAuthenticationProvider());
    }

    /**
     * Password encoder b crypt password encoder.
     * @return the b crypt password encoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
