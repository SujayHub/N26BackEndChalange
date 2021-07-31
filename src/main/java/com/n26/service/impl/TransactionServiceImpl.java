package com.n26.service.impl;

import com.n26.domain.Transaction;
import com.n26.exception.TransactionFromFutureException;
import com.n26.exception.TransactionFromPastException;
import com.n26.repo.TransactionRepo;
import com.n26.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepo transactionRepo;
  private final Clock clock;

  @Override
  public void addTransaction(final Transaction transaction) {
      ZonedDateTime timeRightNow = ZonedDateTime.now(clock);
      if(transaction.getTimestampInUtc().isAfter(timeRightNow)){
          throw new TransactionFromFutureException(transaction);
      }
      ZonedDateTime timestampFrom60SecInPast = timeRightNow.minusSeconds(60L);
    if (transaction.getTimestampInUtc().isBefore(timestampFrom60SecInPast)) {
      throw new TransactionFromPastException(transaction);
    }
    transactionRepo.addTransaction(transaction);
  }

  @Override
  public List<Transaction> getTransactionsInATimeRange(
      ZonedDateTime fromTime, ZonedDateTime toTime) {
    return transactionRepo.getTransactionsInATimeRange(fromTime, toTime);
  }

  @Override
  public void deleteTransactions() {
    transactionRepo.deleteAll();
  }
}
