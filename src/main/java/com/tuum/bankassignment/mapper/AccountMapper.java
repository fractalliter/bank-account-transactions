package com.tuum.bankassignment.mapper;

import com.tuum.bankassignment.entity.Account;
import com.tuum.bankassignment.entity.Balance;
import com.tuum.bankassignment.entity.Transaction;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface AccountMapper {
    @Insert("INSERT INTO accounts(customer_id, country) " +
            " VALUES (#{customerId}, #{country})")
    @Options(useGeneratedKeys=true, keyProperty="accountId")
    int createAccount(Account account);

    @Select("SELECT * FROM accounts WHERE id = #{id}")
    @Results(value = {
            @Result(property = "accountId", column = "id"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "balances", column = "id",
                    javaType = List.class, many = @Many(select = "getBalances", fetchType = FetchType.EAGER))
    })
    Account getAccount(@Param("id") Long id);

    @Select("SELECT account_id, amount, currency FROM balances WHERE account_id = #{accountID}")
    @Results(value = {
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "currency", column = "currency")
    })
    List<Balance> getBalances(Long accountId);
    @Select("SELECT t.id, t.account_id, t.amount, t.currency, t.direction, t.description " +
            "FROM (SELECT * FROM transactions WHERE account_id=#{id}) AS t LEFT JOIN " +
            "(SELECT * FROM accounts WHERE id= #{id}) AS a " +
            "ON a.id = t.account_id " +
            "ORDER BY t.id DESC LIMIT #{limit} OFFSET #{offset}")
    @Results(value = {
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "direction", column = "direction"),
            @Result(property = "description", column = "description"),
    })
    List<Transaction> getTransactions(
            @Param("id") Long id,
            @Param("offset") int offset,
            @Param("limit") Short limit
    );
}
