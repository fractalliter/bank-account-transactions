package com.earl.bank.configurations;

import com.earl.bank.logging.AOPLogging;
import com.earl.bank.logging.RabbitMQClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import static org.mockito.Mockito.mock;

@Configuration
@Profile("test")
public class TestConfigurations {
    @Bean
    @Primary
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("init.sql")
                .build();
    }

    @Bean
    @Primary
    public RabbitMQClient senderMock(){
        return mock(RabbitMQClient.class);
    }

    @Bean
    @Primary
    public AOPLogging aopLoggingMock(){
        return mock(AOPLogging.class);
    }
}
