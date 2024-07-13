package com.earl.bank;

import com.earl.bank.configuration.RabbitMQConfig;
import com.earl.bank.controller.AccountController;
import com.earl.bank.controller.TransactionController;
import com.earl.bank.exception.GlobalExceptionHandler;
import com.earl.bank.logging.AOPLogging;
import com.earl.bank.mapper.AccountMapper;
import com.earl.bank.mapper.BalanceMapper;
import com.earl.bank.mapper.TransactionMapper;
import com.earl.bank.service.AccountService;
import com.earl.bank.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BankAssignmentApplicationTests {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(applicationContext);
        Assertions.assertNotNull(applicationContext.getBean(AccountService.class));
        Assertions.assertNotNull(applicationContext.getBean(TransactionService.class));
        Assertions.assertNotNull(applicationContext.getBean(AccountController.class));
        Assertions.assertNotNull(applicationContext.getBean(TransactionController.class));
        Assertions.assertNotNull(applicationContext.getBean(RabbitMQConfig.class));
        Assertions.assertNotNull(applicationContext.getBean(AOPLogging.class));
        Assertions.assertNotNull(applicationContext.getBean(AccountMapper.class));
        Assertions.assertNotNull(applicationContext.getBean(BalanceMapper.class));
        Assertions.assertNotNull(applicationContext.getBean(TransactionMapper.class));
        Assertions.assertNotNull(applicationContext.getBean(GlobalExceptionHandler.class));
    }

}
