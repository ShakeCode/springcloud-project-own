package com.test.springcloud.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Map;

/**
 * The type Error controller.
 */
@RestController
@SessionAttributes("authorizationRequest")
public class ErrorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    /**
     * Error string.
     * @param parameters the parameters
     * @return the string
     */
    @RequestMapping("/oauth/error")
    public String error(@RequestParam Map<String, String> parameters) {
        String url = parameters.get("redirect_uri");
        LOGGER.info("重定向: {}", url);
        return "redirect:" + url + "?error=1";
    }

}
