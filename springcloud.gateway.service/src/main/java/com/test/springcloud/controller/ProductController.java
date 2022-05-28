package com.test.springcloud.controller;


import com.springcloud.common.model.ResultVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Product controller.
 */
//@RefreshScope
@RequestMapping("v1/product/")
@Api(value = "产品信息接口")
@RestController
public class ProductController {


    /**
     * Gets product data info.
     * @return the product data info
     */
    @GetMapping(value = "/info")
    public ResultVO getProductDataInfo() {
        return ResultVO.success("this is gateway product data service!!!");
    }

}
