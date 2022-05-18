package com.tuum.bankassignment.mapper;

import com.tuum.bankassignment.dto.CreateAccountDTO;
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
    public int createAccount(Account account);
    @Select("SELECT * FROM accounts WHERE id = #{id}")
    @Results(value = {
            @Result(property = "accountId", column = "id"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "balances", column = "id",
                    javaType = List.class, many = @Many(select = "getBalances", fetchType = FetchType.EAGER))
    })
    public Account getAccount(@Param("id") Long id);

    @Select("SELECT * FROM balances WHERE account_id = #{accountID}")
    @Results(value = {
            @Result(property = "currency", column = "currency"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "accountId", column = "account_id")
    })
    List<Balance> getBalances(Long accountId);
    @Select("SELECT * FROM accounts LEFT JOIN " +
            "(SELECT * FROM transactions WHERE account_id= #{id}) AS t " +
            "ON id = t.account_id")
    public List<Transaction> getTransactions(
            @Param("id") Long id,
            @Param("offset") int offset,
            @Param("limit") Short limit
    );
}
