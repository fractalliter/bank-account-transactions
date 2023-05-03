package com.earl.bank.mapper;

import com.earl.bank.entity.Balance;
import com.earl.bank.entity.Currency;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface BalanceMapper {
    @Insert("INSERT INTO balances(currency, amount, account_id)" +
            "VALUES(#{currency}::valid_currencies, #{amount},#{accountId})")
    void createBalance(Balance balance);

    @Update("UPDATE balances SET amount= amount+#{amount} " +
            "WHERE account_id=#{accountId} AND currency=#{currency}::valid_currencies")
    void increaseBalance(
            @Param("accountId") Long accountId,
            @Param("currency") Currency currency,
            @Param("amount") BigDecimal amount
    );

    @Update("UPDATE balances SET amount= amount-#{amount} " +
            "WHERE account_id=#{accountId} AND currency=#{currency}::valid_currencies AND amount > 0")
    void decreaseBalance(
            @Param("accountId") Long accountId,
            @Param("currency") Currency currency,
            @Param("amount") BigDecimal amount
    );

    @Select("SELECT account_id, amount, currency FROM balances WHERE account_id=#{accountId} AND currency=#{currency}::valid_currencies")
    @Results(value = {
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "currency", column = "currency"),
    })
    Balance getBalance(Long accountId, Currency currency);
}
