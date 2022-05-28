package com.springcloud.common.mock;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * The type Mock controller.
 */
@RequestMapping("v1/")
@Api("mock服务器-代理相关接口")
@RestController
public class MockController {

    private final MockService mockService;

    /**
     * Instantiates a new Mock controller.
     * @param mockService the mock service
     */
    public MockController(MockService mockService) {
        this.mockService = mockService;
    }

    /**
     * Mock test.
     * @param request  the request
     * @param response the response
     */
    @RequestMapping("/mock/**")
    public void mockTest(HttpServletRequest request, HttpServletResponse response) {
        mockService.dynamicMock(request, response);
    }

}
