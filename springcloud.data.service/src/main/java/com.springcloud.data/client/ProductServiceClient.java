package com.springcloud.data.client;

import com.springcloud.common.model.ResultVO;
import com.springcloud.data.fallback.ProductFallbackServiceImpl;
import com.springcloud.data.model.CamelCaseUser;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The interface Product service client.
 */
@Configuration
@FeignClient(name = "EUREKA-COMMON-SERVICE", fallback = ProductFallbackServiceImpl.class)
public interface ProductServiceClient {

    /**
     * 查询
     * @param name the name
     * @return service info
     */
    @GetMapping("/v1/data/world")
    ResultVO getProductInfo(@RequestParam("name") String name);

    /**
     * 查询
     * @param userName the user name
     * @return service info
     */
    @GetMapping("/v1/data/user")
    ResultVO<CamelCaseUser> getUserInfo(@RequestParam("userName") String userName);
}
