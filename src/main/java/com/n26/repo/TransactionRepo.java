package com.n26.repo;

import com.n26.domain.Transaction;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionRepo {
    void addTransaction(final @NotNull Transaction transaction);
    List<Transaction> getTransactionsInATimeRange(final ZonedDateTime fromTime, final ZonedDateTime toTime);
    void deleteAll();
}
