package com.springcloud.common.controller;


import com.springcloud.common.model.ResultVO;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * The type Data controller.   http://127.0.0.1:8091/search/v1/data/song/get
 */
@RequestMapping("v1/data/")
@Api("test")
@RestController
public class DataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    @Value("${spring.application.name}")
    private String instantName;

    @Value("${server.port}")
    private String port;

    /**
     * Hello world result vo.
     * @param name the name
     * @return the result vo
     */
    @RequestMapping(value = "/world", method = RequestMethod.GET)
    public ResultVO helloWorld(String name) {
//        int a = 1 / 0;
        LOGGER.info("传入的值为: {}", name);
        return ResultVO.success("传入的值为:".concat(name), "当前服务名:" + instantName + " 当前端口:" + port);
    }

    /**
     * Gets user.
     * @param userName the user name
     * @return the user
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResultVO getUser(String userName) {
        LOGGER.info("传入userName的值为: {}", userName);
        Map<String, Object> map = new HashMap<>(3);
        /*map.put("user_name", "lidream");
        map.put("user_id", "123");
        map.put("user_code", "475784");
        */
        map.put("userName", "lidream");
        map.put("userId", "123");
        map.put("userCode", "475784");
        return ResultVO.successData(map);
    }


    /**
     * Verify static mcok boolean.
     * @param userName the user name
     * @return the boolean
     */
    public boolean verifyStaticMcok(String userName) {
        boolean flag = isTrue(userName);
        System.out.println("校验用户名：" + flag);
        return flag;
    }

    /**
     * 静态方法 * *
     * @param userName 用户名不能为空
     * @return the boolean
     */
    public static final boolean isTrue(String userName) {
        if (userName == null || "".equals(userName)) {
            return false;
        }
        return true;
    }
}
