package com.tuum.bankassignment.loging;

import com.tuum.bankassignment.dto.CreateAccountDTO;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AOPLogging {

    @Autowired
    RabbitMQSender rabbitMQSender;

    @AfterReturning(value = "execution(* com.tuum.bankassignment.service.AccountService.createAccount*(..))", returning = "result")
    public void afterCreateAccount(JoinPoint joinPoint, Object result) {
        System.out.println("After method:" + joinPoint.getSignature());
        System.out.println("Creating account with first name"+result.toString());
        rabbitMQSender.send(result);
    }
}
