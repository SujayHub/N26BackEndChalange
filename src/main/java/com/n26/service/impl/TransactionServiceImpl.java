package com.n26.service.impl;

import com.n26.domain.Transaction;
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
  public boolean addTransaction(final Transaction transaction) {
      ZonedDateTime timestampFrom60SecInPast = ZonedDateTime.now(clock).minusSeconds(60L);
      if (transaction.getTimestampInUtc().isBefore(timestampFrom60SecInPast)) {
      return false;
    }
      transactionRepo.addTransaction(transaction);
      return true;
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