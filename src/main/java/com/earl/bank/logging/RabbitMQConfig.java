package com.earl.bank.logging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
public class RabbitMQConfig {
    @Bean
    Queue queue(@Value("${bank.rabbitmq.queue}") String queueName) {
        return new Queue(queueName, false);
    }

    @Bean
    DirectExchange exchange(@Value("${bank.rabbitmq.exchange}") String exchange) {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange, @Value("${bank.rabbitmq.routingkey}") String routingkey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitMQClient sender(RabbitTemplate template) {
        return new RabbitMQClient(template);
    }
}
