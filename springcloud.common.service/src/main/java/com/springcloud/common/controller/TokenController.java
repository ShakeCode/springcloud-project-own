package com.springcloud.common.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springcloud.common.model.ResultVO;
import com.springcloud.common.model.User;
import com.springcloud.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


/**
 * The type Token controller.
 * http://127.0.0.1:8192/v1/auth/token/get
 */
@RequestMapping("v1/auth/")
@Api("令牌服务")
@RestController
public class TokenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);

    /**
     * Hello world result vo.
     * @return the result vo
     */
    @RequestMapping(value = "/token/get", method = RequestMethod.GET)
    public ResultVO getToken() {
        User user = new User("dreamLi", UUID.randomUUID().toString());
        String token = JwtUtil.createJwtToken("1", user.getUserName(), JSON.toJSONString(user));
        return ResultVO.successData(token);
    }

    /**
     * Gets user by token.
     * @param request the request
     * @return the user by token
     */
    @RequestMapping(value = "/who", method = RequestMethod.GET)
    public ResultVO getUserByToken(HttpServletRequest request) {
        String authorization = JwtUtil.getRequestToken(request);
        Claims claims = JwtUtil.parseJwtToken(authorization);
        return ResultVO.successData(JSONObject.parseObject(claims.get("user-info", String.class), User.class));
    }
}
