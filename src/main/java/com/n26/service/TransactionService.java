package com.n26.service;

import com.n26.domain.Transaction;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionService {

    void addTransaction(final @NotNull Transaction transaction);
    List<Transaction> getTransactionsInATimeRange(final ZonedDateTime fromTime, final ZonedDateTime toTime);
    void deleteTransactions();
}
