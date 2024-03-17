package com.earl.bank.logging;

import com.earl.bank.configurations.RabbitMQSender;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AOPLogging {
    private final RabbitMQSender rabbitMQSender;

    @Autowired
    public AOPLogging(RabbitMQSender rabbitMQSender) {
        this.rabbitMQSender = rabbitMQSender;
    }

    @AfterReturning(
            value = "execution(* com.earl.bank.service.AccountService.*(..))",
            returning = "result"
    )
    public void afterCreateAccount(Object result) {
        rabbitMQSender.send(result);
    }

    @AfterReturning(
            value = "execution(* com.earl.bank.service.TransactionService.*(..))",
            returning = "result"
    )
    public void afterCreateTransaction(Object result) {
        rabbitMQSender.send(result);
    }
}
