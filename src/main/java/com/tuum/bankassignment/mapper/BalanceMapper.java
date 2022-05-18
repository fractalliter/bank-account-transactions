package com.tuum.bankassignment.mapper;

import com.tuum.bankassignment.entity.Balance;
import com.tuum.bankassignment.entity.Currency;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BalanceMapper {
    @Insert("INSERT INTO balances(currency, amount, account_id)" +
            "VALUES(#{currency}::valid_currencies, #{amount},#{accountId})")
    void createBalance(Balance balance);

    @Update("UPDATE balances SET amount= amount+#{amount} " +
            "WHERE account_id=#{accountId} AND currency=#{currency}")
    void increaseBalance(
            @Param("account_id") Long accountId,
            @Param("currency")Currency currency,
            @Param("amount") Long amount
    );

    @Update("UPDATE balances SET amount= amount-#{amount} " +
            "WHERE account_id=#{accountId} AND currency=#{currency} AND amount > 0")
    void decreaseBalance(
            @Param("account_id") Long accountId,
            @Param("currency")Currency currency,
            @Param("amount") Long amount
    );
}
