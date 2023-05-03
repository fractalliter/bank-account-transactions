package com.earl.bank.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AOPLogging {
    private final static Logger log = LoggerFactory.getLogger(AOPLogging.class);
    private final RabbitMQSender rabbitMQSender;

    @Autowired
    public AOPLogging(RabbitMQSender rabbitMQSender) {
        this.rabbitMQSender = rabbitMQSender;
    }

    @AfterReturning(
            value = "execution(* com.earl.bank.service.AccountService.*(..))",
            returning = "result"
    )
    public void afterCreateAccount(JoinPoint joinPoint, Object result) {
        log.debug(joinPoint.getSignature() + " " + result.toString());
        rabbitMQSender.send(result);
    }

    @AfterReturning(
            value = "execution(* com.earl.bank.service.TransactionService.*(..))",
            returning = "result"
    )
    public void afterCreateTransaction(JoinPoint joinPoint, Object result) {
        log.debug(joinPoint.getSignature() + " " + result.toString());
        rabbitMQSender.send(result);
    }
}
