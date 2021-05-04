package com.n26.repo.impl;

import com.n26.domain.Transaction;
import com.n26.repo.TransactionRepo;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Repository
public class TransactionRepoImpl implements TransactionRepo {

  private final Collection<Transaction> transactions = new ConcurrentLinkedQueue<>();

  @Override
  public void addTransaction(final Transaction transaction) {
    transactions.add(transaction);
  }

  @Override
  public List<Transaction> getTransactionsInATimeRange(
      final ZonedDateTime fromTime, final ZonedDateTime toTime) {
    return transactions.stream()
        .filter(
            transaction ->
                !transaction.getTimestampInUtc().isBefore(fromTime)
                    && !transaction.getTimestampInUtc().isAfter(toTime))
        .collect(Collectors.toList());
  }

  @Override
  public void deleteAll() {
    transactions.clear();
  }
}
