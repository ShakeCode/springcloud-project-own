package com.springcloud.data.advice;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.springcloud.data.annotation.SecurityParameter;
import com.springcloud.data.util.AesEncryptUtils;
import com.springcloud.data.util.RSAUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * The type Decode request body advice.  请求数据解密
 */
@ControllerAdvice(basePackages = "com.springcloud.data.controller")
public class DecodeRequestBodyAdvice implements RequestBodyAdvice {
    private static final Logger logger = LoggerFactory.getLogger(DecodeRequestBodyAdvice.class);

    @Value("${server.private.key}")
    private String SERVER_PRIVATE_KEY;

    //private static final String SERVER_PRIVATE_KEY="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIn2zWqU7K/2qm5pOpq5bp9R+3MTnStWTfJU9nC/Vo7UKH9dITPvrELCTK+qlqpx5Fes+l0GY7n6u4n4jyiw4ejsvkZYQ5ww477yLOn2FcoEGuZEwPgSCmfTST0OFUgQqn+/J11k9L92jEHyieE3qmhMkMt0UsVUSJwx/nZxo30ZAgMBAAECgYBD3YHigeuEC4R+14iaf8jo2j0kuGtB3Cxvnlez0otTqw1YyYkBsU49cLKkXvfKVEgM0Ow/QltgKvSBxCE31PrrDka5TygVMqqA/IM7NrDvjUcGLjyoeNmLA8660fWcDxUTlAGN5kxIvUATayVwKVflpWPWu0FPKsWrZustnEo+4QJBAMCmYsWqAKWYMVRXFP3/XGRfio8DV793TOckyBSN9eh8UhgoZyT3u7oeHmDJEwm4aNMHlg1Pcdc6tNsvi1FRCiUCQQC3VNzfF4xOtUgX7vWPL8YVljLuXmy12iVYmg6ofu9l31nwM9FLQ1TRFglvF5LWrIXTQb07PgGd5DJMAQWGsqLlAkAPE7Z9M73TN+L8b8hDzJ1leZi1cpSGdoa9PEKwYR/SrxAZtefEm+LEQSEtf+8OfrEtetWCeyo0pvKKiOEFXytFAkEAgynL/DC0yXsZYUYtmYvshHU5ayFTVagFICbYZeSrEo+BoUDxdI9vl0fU6A5NmBlGhaZ65G+waG5jLc1tTrlvoQJAXBEoPcBNAosiZHQfYBwHqU6mJ9/ZacJh3MtJzGGebfEwJgtln5b154iANqNWXpySBLvkK+Boq7FYRiD83pqmUg==";

    /**
     * Supports boolean.
     * @param methodParameter the method parameter
     * @param type            the type
     * @param aClass          the a class
     * @return the boolean
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * Handle empty body object.
     * @param body             the body
     * @param httpInputMessage the http input message
     * @param methodParameter  the method parameter
     * @param type             the type
     * @param aClass           the a class
     * @return the object
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    /**
     * Before body read http input message.
     * @param inputMessage    the input message
     * @param methodParameter the method parameter
     * @param type            the type
     * @param aClass          the a class
     * @return the http input message
     * @throws IOException the io exception
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        try {
            boolean encode = false;
            if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
                //获取注解配置的包含和去除字段
                SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
                //入参是否需要解密
                encode = serializedField.inDecode();
            }
            if (encode) {
                logger.info("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密");
                return new MyHttpInputMessage(inputMessage);
            } else {
                return inputMessage;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常：" + e.getMessage());
            return inputMessage;
        }
    }

    /**
     * After body read object.
     * @param body             the body
     * @param httpInputMessage the http input message
     * @param methodParameter  the method parameter
     * @param type             the type
     * @param aClass           the a class
     * @return the object
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    /**
     * The type My http input message.
     */
    class MyHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        /**
         * Instantiates a new My http input message.
         * @param inputMessage the input message
         * @throws Exception the exception
         */
        public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            this.body = IOUtils.toInputStream(easpString(IOUtils.toString(inputMessage.getBody(), StandardCharsets.UTF_8)));
        }

        /**
         * Gets body.
         * @return the body
         * @throws IOException the io exception
         */
        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        /**
         * Gets headers.
         * @return the headers
         */
        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        /**
         * Easp string string.
         * @param requestData the request data
         * @return string
         */
        public String easpString(String requestData) {
            if (requestData != null && !requestData.equals("")) {
                Map<String, String> map = new Gson().fromJson(requestData, new TypeToken<Map<String, String>>() {
                }.getType());
                // 密文
                String data = map.get("requestData");
                // 加密的ase秘钥
                String encrypted = map.get("encrypted");
                if (StringUtils.isEmpty(data) || StringUtils.isEmpty(encrypted)) {
                    throw new RuntimeException("参数【requestData】缺失异常！");
                } else {
                    String content = null;
                    String aseKey = null;
                    try {
                        aseKey = RSAUtils.decryptDataOnJava(encrypted, SERVER_PRIVATE_KEY);
                    } catch (Exception e) {
                        throw new RuntimeException("参数【aseKey】解析异常！");
                    }
                    try {
                        content = AesEncryptUtils.decrypt(data, aseKey);
                    } catch (Exception e) {
                        throw new RuntimeException("参数【content】解析异常！");
                    }
                    if (StringUtils.isEmpty(content) || StringUtils.isEmpty(aseKey)) {
                        throw new RuntimeException("参数【requestData】解析参数空指针异常!");
                    }
                    return content;
                }
            }
            throw new RuntimeException("参数【requestData】不合法异常！");
        }
    }
}
