package com.earl.bank.logging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitMQSender {
    private final RabbitTemplate template;

    public RabbitMQSender(RabbitTemplate template) {
        this.template = template;
    }

    public void send(Object message) {
        this.template.convertAndSend(message);
    }
}
