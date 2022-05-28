package com.springcloud.data.service;

import com.springcloud.common.model.ResultVO;
import com.springcloud.data.client.ProductServiceClient;
import com.springcloud.data.model.CamelCaseUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The type Product service.
 */
@Service
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final ProductServiceClient productServiceClient;

    /**
     * Instantiates a new Product service.
     * @param productServiceClient the product service client
     */
    public ProductService(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    /**
     * Gets product info.
     * @param name the name
     * @return the product info
     */
    public ResultVO getProductInfo(String name) {
        ResultVO resultVO = productServiceClient.getProductInfo(name);
        LOGGER.info("invoke common service return:{}", resultVO);
        return resultVO;
    }

    /**
     * Gets product info.
     * @param name the name
     * @return the product info
     */
    public ResultVO<CamelCaseUser> getUserInfo(String name) {
        ResultVO<CamelCaseUser> resultVO = productServiceClient.getUserInfo(name);
        LOGGER.info("invoke common service return:{}", resultVO);
        return resultVO;
    }
}
