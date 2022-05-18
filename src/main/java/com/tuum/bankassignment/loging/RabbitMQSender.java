package com.tuum.bankassignment.loging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class RabbitMQSender {
    @Autowired
    private RabbitTemplate template;

    @Value("${bank.rabbitmq.exchange}")
    private String exchange;

    @Value("${bank.rabbitmq.routingkey}")
    private String routingkey;

    public void send(Object message) {
        this.template.convertAndSend(exchange, routingkey, message);
    }
}
