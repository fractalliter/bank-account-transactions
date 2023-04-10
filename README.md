# Bank Account Transactions Microservice
A Bank account transactions microservice with Spring Boot, Postgres, and RabbitMQ.
This microservice is responsible for creating user account and balances for provided currencies that are defined
as an `enum`:

```java
public enum Currency{
    EUR,SEK,GBP,USD
}
```

Users can **deposit** and **withdraw** money from their accounts by providing a Direction:

```java
public enum Direction {
    IN,OUT
}
```

All transactions will be executed with proper isolation level.

## Ingredients

- [Java 11](https://jdk.java.net/11/)
- [Junit5](https://junit.org/junit5/docs/current/user-guide/)
- [Gradle](https://gradle.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [myBATIS 3](https://mybatis.org/mybatis-3/)
- [Postgres](https://www.postgresql.org/)
- [RabbitMQ](https://www.rabbitmq.com/)
- [Docker](https://www.docker.com/)

## How to deploy
You need to have docker installed on your machine. you can easily deploy the service with executing
following command in your bash terminal:

```bash
docker compose up
```
It will create and start **database** and **message broker** services first and then it will create
a container form the bank account microservice docker image and will bind port 8080 of the container for public use.

## How to use
Hit the port 8080 for the good reasons.

## Todo
- [ ] add documentation
- [ ] add benchmark
- [ ] add integration test for transactions
