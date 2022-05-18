package com.tuum.bankassignment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class BankAssignmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankAssignmentApplication.class, args);
    }

}
