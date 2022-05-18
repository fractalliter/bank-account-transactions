package com.tuum.bankassignment.mapper;

import com.tuum.bankassignment.dto.CreateTransactionDTO;
import com.tuum.bankassignment.entity.Transaction;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TransactionMapper {
    @Insert("INSERT INTO transactions(account_id, amount, currency, direction, description) " +
            "VALUES(#{accountId},#{amount},#{currency}::valid_currencies,#{direction}::valid_directions,#{description})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void createTransaction(Transaction transaction);

    @Select("SELECT * FROM transactions WHERE id=#{transactionId}")
    Transaction getTransaction(@Param("transactionId") Long transactionId);
}
