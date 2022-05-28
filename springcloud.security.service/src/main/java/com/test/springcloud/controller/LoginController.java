package com.test.springcloud.controller;


import com.springcloud.common.model.ResultVO;
import com.test.springcloud.service.VerifyRsaTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Test controller.
 */
@RestController
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private final VerifyRsaTokenService verifyRsaTokenService;

    /**
     * Instantiates a new Login controller.
     * @param verifyRsaTokenService the verify rsa token service
     */
    public LoginController(VerifyRsaTokenService verifyRsaTokenService) {
        this.verifyRsaTokenService = verifyRsaTokenService;
    }

    /**
     * Hello login map.
     * @return the map
     */
    @GetMapping("/")
    public ResultVO helloLogin() {
        Map<String, String> map = new HashMap<>(1);
        map.put("content", "hello dream");
        return ResultVO.successData(map);
    }

    /**
     * Login result vo.
     * @param request the request
     * @return the result vo
     */
    @GetMapping("/who")
    public ResultVO login(HttpServletRequest request) {
        // 校验对称加密HmacSHA512
        // Authentication authentication = TokenAuthenticationService.verifyAuthentication(request);
        // 解析RSA非对称token
        Authentication authentication = verifyRsaTokenService.verifyRsaToken(request);
        LOGGER.info("user authentication:{}", authentication);
        return ResultVO.successData(authentication);
    }

}
