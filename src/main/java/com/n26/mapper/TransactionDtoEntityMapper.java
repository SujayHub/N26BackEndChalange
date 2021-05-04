package com.n26.mapper;

import com.n26.domain.Transaction;
import com.n26.dto.TransactionDto;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class TransactionDtoEntityMapper implements DtoEntityMapper<TransactionDto, Transaction> {
  @Override
  public Transaction toEntity(@NotNull final TransactionDto transactionDto) {
    return Transaction.builder()
        .amount(new BigDecimal(transactionDto.getAmount()))
        .timestampInUtc(
            ZonedDateTime.ofInstant(transactionDto.getTimestamp().toInstant(), ZoneOffset.UTC))
        .build();
  }

  @Override
  public TransactionDto toDto(@NotNull final Transaction transaction) {
    return TransactionDto.builder()
        .amount(transaction.getAmount().toString())
        .timestamp(Date.from(transaction.getTimestampInUtc().toInstant()))
        .build();
  }
}
