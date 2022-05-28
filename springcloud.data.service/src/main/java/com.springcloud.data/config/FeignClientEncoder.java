package com.springcloud.data.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import feign.FeignException;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.EncodeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class FeignClientEncoder extends SpringEncoder {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public FeignClientEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {

        try {
            super.encode(object, bodyType, template);
            template.body(objectMapper.writeValueAsString(object));
        } catch (Exception ex) {

        }

        System.out.println();
    }

    public static class FeignClientDecoder extends SpringDecoder {
        public FeignClientDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
            super(messageConverters);
        }

        @Override
        public Object decode(Response response, Type type) throws IOException, FeignException {
            String result = response.body().toString();
            if (type instanceof Class && StringUtils.isNotBlank(result)) {
                Response build = Response.builder()
                        .body(new ObjectMapper().writeValueAsString(objectMapper.readValue(result, (Class<? extends Object>) type)), Charset.defaultCharset())
                        .headers(response.headers())
                        .reason(response.reason())
                        .status(response.status())
                        .request(response.request())
                        .build();
                return super.decode(build, type);
            }
            return super.decode(response, type);
        }
    }
}