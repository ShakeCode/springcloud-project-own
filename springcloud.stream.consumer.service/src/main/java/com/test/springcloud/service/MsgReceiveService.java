package com.test.springcloud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;


/**
 * The type Msg receive service.
 */
@Service
@EnableBinding(MsgSinkService.class)
public class MsgReceiveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgReceiveService.class);

    /**
     * Recieve.
     * @param payload the payload
     */
    @StreamListener("myInput")
    public void receiveMsg(Object payload) {
        LOGGER.info("receive msg:{}", payload);
    }
}