# Bank Account Transactions Microservice

A Bank account transactions microservice with Spring Boot, Postgres as Database, and RabbitMQ for async communication.
This microservice is responsible for creating user account and balances for provided currencies that are defined
as an `enum`:

```java
public enum Currency {
    EUR, SEK, GBP, USD
}
```

Users can **deposit** and **withdraw** money from accounts by providing a Direction:

```java
public enum Direction {
    IN, OUT
}
```

Project uses Spring AOP to log the result of every transaction which improves the efficiency of main business
logic by keeping it clean.

All transactions will be executed with proper isolation level.

## Ingredients

- [Java 11](https://jdk.java.net/11/)
- [Junit5](https://junit.org/junit5/docs/current/user-guide/)
- [Gradle](https://gradle.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring AOP](https://docs.spring.io/spring-framework/reference/core/aop.html)
- [myBATIS 3](https://mybatis.org/mybatis-3/)
- [Postgres](https://www.postgresql.org/)
- [RabbitMQ](https://www.rabbitmq.com/)
- [Docker](https://www.docker.com/)

## How to deploy

You need to have docker installed on your machine. you can easily deploy the service with executing
the following command in your bash terminal:

```bash
docker compose up
```

It will create and start **database** and **message broker** services first and then it will create
a container form the bank account microservice docker image and will bind port 8080 of the container for public use.

## How to use

Hit port 8080 for good reasons.

### API page

Go to [OpenAPI](http://localhost:8080/swagger-ui/index.html) documents page and hit the endpoints.

## How to Test

The Main business logic is covered with testing to a good extent, but there are also rooms for improvement.

```bash
./gradlew test
```

## Todo

- [ ] add benchmark
- [x] add integration test for transactions
- [ ] add java docs
- [ ] add build status and test coverage to pull requests
- [ ] add project status to readme