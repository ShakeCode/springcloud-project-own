package com.springcloud.data.fallback;

import com.springcloud.common.model.ResultVO;
import com.springcloud.data.client.ProductServiceClient;
import org.springframework.stereotype.Component;

/**
 * The type Product fallback service. 产品服务熔断处理类
 */
@Component
public class ProductFallbackServiceImpl implements ProductServiceClient {

    @Override
    public ResultVO getProductInfo(String name) {
        return ResultVO.fail("获取产品异常,请稍后再试试");
    }

    @Override
    public ResultVO getUserInfo(String name) {
        return ResultVO.fail("获取产品异常,请稍后再试试");
    }
}
