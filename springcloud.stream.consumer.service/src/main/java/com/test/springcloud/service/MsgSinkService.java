package com.test.springcloud.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * The interface Msg sink service.
 */
@Component
public interface MsgSinkService {

    /**
     * Receive msg subscribable channel.
     * @return the subscribable channel
     */
    @Input("myInput")
    SubscribableChannel receiveMsg();
}
