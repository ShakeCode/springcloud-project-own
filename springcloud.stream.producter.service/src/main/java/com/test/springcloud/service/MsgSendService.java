package com.test.springcloud.service;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.MessageBuilder;

/**
 * The type Msg send service.
 */
@EnableBinding({MessageSourceService.class})
public class MsgSendService {

    /*官方预制处理器*/
    private final Processor processor;

    private final MessageSourceService messageSourceService;

    /**
     * Instantiates a new Msg send service.
     * @param processor            the processor
     * @param messageSourceService the message source service
     */
    public MsgSendService(Processor processor, MessageSourceService messageSourceService) {
        this.processor = processor;
        this.messageSourceService = messageSourceService;
    }

    /**
     * Send msg.
     * @param msg the msg
     */
    public void sendMsg(String msg) {
        messageSourceService.send().send(MessageBuilder.withPayload(msg).build());
    }


    public boolean write(String data) {
        return processor.output().send(MessageBuilder.withPayload(data).build());
    }

    /**
     * Subscribe boolean.
     * @param handler the handler
     * @return the boolean
     */
    public boolean subscribe(MessageHandler handler) {
        return processor.input().subscribe(handler);
    }
}
