package com.test.springcloud.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * The interface Message source service.
 */
@Component
public interface MessageSourceService {

    /**
     * Send message channel.
     * @return the message channel
     */
    @Output("myOutput")
    MessageChannel send();
}
