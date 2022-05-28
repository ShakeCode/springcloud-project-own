package com.springcloud.data.controller;


import com.springcloud.common.model.ResultVO;
import com.springcloud.data.annotation.RepeatRequestLimit;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Repeat test data controller.
 */
@RequestMapping("v1/repeat/")
@Api(value = "防重复提交测试")
@RestController
public class RepeatTestDataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepeatTestDataController.class);

    /**
     * Gets product data info.
     * @return the product data info
     */
    @RepeatRequestLimit
    @GetMapping(value = "/test")
    public ResultVO getProductDataInfo() {
        return ResultVO.success("this is product data service!!!");
    }
}
