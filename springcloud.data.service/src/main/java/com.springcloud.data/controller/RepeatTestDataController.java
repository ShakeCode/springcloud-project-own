package com.springcloud.data.controller;


import com.springcloud.common.model.ResultVO;
import com.springcloud.common.model.User;
import com.springcloud.data.annotation.RepeatRequestLimit;
import com.springcloud.data.annotation.SecurityParameter;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


/**
 * The type Repeat test data controller.
 */
@RequestMapping("v1/repeat/")
@Api(value = "防重复提交测试")
@RestController
public class RepeatTestDataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepeatTestDataController.class);

    /**
     * Test encrypt user. 测试加解密消息返回
     * @param user the user
     * @return the user
     */
    @RequestMapping("/testEncrypt")
    @ResponseBody
    @SecurityParameter
    public User testEncrypt(@RequestBody User user) {
        System.out.println(user.getUserName());
        String content = "内容";
        user.setUserId(UUID.randomUUID().toString());
        return user;
    }

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
