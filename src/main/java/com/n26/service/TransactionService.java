package com.n26.service;

import com.n26.domain.Transaction;
import java.time.ZonedDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;

public interface TransactionService {

  void addTransaction(final @NotNull Transaction transaction);

  List<Transaction> getTransactionsInATimeRange(
      final ZonedDateTime fromTime, final ZonedDateTime toTime);

  void deleteTransactions();
}
