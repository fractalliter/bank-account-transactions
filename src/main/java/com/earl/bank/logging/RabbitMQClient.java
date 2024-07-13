package com.earl.bank.logging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitMQClient {
    private final RabbitTemplate template;

    public RabbitMQClient(RabbitTemplate template) {
        this.template = template;
    }

    public void send(Object message) {
        this.template.convertAndSend(message);
    }
}
