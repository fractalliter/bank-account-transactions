package com.tuum.bankassignment.mapper;

import com.tuum.bankassignment.dto.CreateTransactionDTO;
import com.tuum.bankassignment.entity.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TransactionMapper {
    @Insert("INSERT INTO transactions(account_id, amount, currency, direction, description) " +
            "VALUES(#{accountId},#{amount},#{currency},#{direction},#{description})")
    void createTransaction(Transaction transaction);

    @Select("SELECT * FROM transactions WHERE id=#{transactionId}")
    Transaction getTransaction(@Param("transactionId") Long transactionId);
}
