package com.springcloud.data.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.springcloud.data.interceptor.RepeatRequestInterceptor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * The type My web mvc config.
 *
 * actuator监听数据API：http://127.0.0.1:8092/actuator
 *
 * 监听流: http://127.0.0.1:8092/actuator/hystrix.stream
 *
 * 可视化熔断管理页面：http://127.0.0.1:8092/hystrix , 进入页面后输入监听流API
 */
@Configuration
public class MyWebMvcConfig extends WebMvcConfigurationSupport {

    private final RepeatRequestInterceptor repeatRequestInterceptor;

    /**
     * Instantiates a new My web mvc config.
     * @param repeatRequestInterceptor the repeat request interceptor
     */
    public MyWebMvcConfig(RepeatRequestInterceptor repeatRequestInterceptor) {
        this.repeatRequestInterceptor = repeatRequestInterceptor;
    }

    /**
     * 在 Spring Boot 1.3版本中，会默认提供一个RestTemplate的实例Bean，而在 Spring Boot 1.4以及以后的版本中，这个默认的bean不再提供了，我们需要在Application启动时，手动创建一个RestTemplate的配置。
     * Gets rest template.注册负载均衡 RestTemplate
     * 不生效, 到启动类生效
     * @return the rest template
     */
    @Bean(value = "AppRestTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * Gets servlet.
     * @return the servlet
     */
    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }

    /**
     * 功能描述:
     * 配置静态资源,避免静态资源请求被拦截
     * @param registry the registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/templates/");
        super.addResourceHandlers(registry);
    }

    /**
     * Add interceptors.
     * @param registry the registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(repeatRequestInterceptor).addPathPatterns("/*");
        super.addInterceptors(registry);
    }

    /**
     * 使用fastjson代替jackson
     *          //先把JackSon的消息转换器删除.
     *         //(1)源码分析可知，返回json的过程为:
     *         //  Controller调用结束后返回一个数据对象，for循环遍历conventers，找到支持application/json的HttpMessageConverter，然后将返回的数据序列化成json。
     *         //  具体参考org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor的writeWithMessageConverters方法
     *         //(2)由于是list结构，我们添加的fastjson在最后。因此必须要将jackson的转换器删除，不然会先匹配上jackson，导致没使用fastjson
     * @param converters the converters
     */
//    @Override
//    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        for (int i = converters.size() - 1; i >= 0; i--) {
//            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
//                converters.remove(i);
//            }
//        }
//        //自定义fastjson配置
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(
//                // 是否输出值为null的字段,默认为false,我们将它打开
//                SerializerFeature.WriteMapNullValue,
//                // 将Collection类型字段的字段空值输出为[]
//                SerializerFeature.WriteNullListAsEmpty,
//                // 将字符串类型字段的空值输出为空字符串
//                SerializerFeature.WriteNullStringAsEmpty,
//                // 将数值类型字段的空值输出为0
//                SerializerFeature.WriteNullNumberAsZero,
//                SerializerFeature.WriteDateUseDateFormat,
//                // 禁用循环引用
//                SerializerFeature.DisableCircularReferenceDetect
//        );
//        // 转换驼峰
//        SerializeConfig config = new SerializeConfig();
//        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
//        config.put(JSON.class, new SwaggerJsonSerializer());
//        fastJsonConfig.setSerializeConfig(config);
//
//        // 添加支持的MediaTypes;不添加时默认为*/*,也就是默认支持全部
//        // 但是MappingJackson2HttpMessageConverter里面支持的MediaTypes为application/json
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON);
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//
//        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
//        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
//        converters.add(fastJsonHttpMessageConverter);
//        //支持XML格式的请求
//        converters.add(new StringHttpMessageConverter());
//    }

}
