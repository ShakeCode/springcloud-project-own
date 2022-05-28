package com.test.springcloud.controller;

import com.springcloud.common.model.ResultVO;
import com.test.springcloud.service.MsgSendService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Stream producer controller.
 */
@RequestMapping("v1/msg")
@Api(value = "发送消息接口")
@RestController
public class StreamProducerController {

    private final MsgSendService sendService;

    /**
     * Instantiates a new Stream producer controller.
     * @param sendService the send service
     */
    public StreamProducerController(MsgSendService sendService) {
        this.sendService = sendService;
    }

    /**
     * Send.
     * @param msg the msg
     */
    @GetMapping(value = "/send/{msg}")
    public ResultVO send(@PathVariable("msg") String msg) {
        sendService.sendMsg(msg);
        return ResultVO.success("send success");
    }
}