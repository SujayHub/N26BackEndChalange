package com.n26.repo.impl;

import static org.junit.Assert.assertEquals;

import com.n26.domain.Transaction;
import com.n26.repo.TransactionRepo;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TransactionRepoImplTest {

  private TransactionRepo transactionRepo;

  @Before
  public void setUp() {
    transactionRepo = new TransactionRepoImpl();
  }

  @Test
  public void shouldFindTransactionsInATimeRange() {

    Transaction transaction1 =
        new Transaction(
            BigDecimal.TEN,
            ZonedDateTime.of(LocalDateTime.of(2021, 5, 2, 10, 20, 0), ZoneOffset.UTC));

    Transaction transaction2 =
        new Transaction(
            BigDecimal.ONE,
            ZonedDateTime.of(LocalDateTime.of(2021, 5, 2, 13, 5, 0), ZoneOffset.UTC));

    transactionRepo.addTransaction(transaction1);
    transactionRepo.addTransaction(transaction2);

    List<Transaction> transactionsInATimeRange =
        transactionRepo.getTransactionsInATimeRange(
            ZonedDateTime.of(2021, 5, 2, 9, 30, 0, 0, ZoneId.of("UTC")),
            ZonedDateTime.of(2021, 5, 2, 12, 30, 0, 0, ZoneId.of("UTC")));

    assertEquals(Collections.singletonList(transaction1), transactionsInATimeRange);
  }

  @Test
  public void shouldDeleteAllTransactions() {
    Transaction transaction1 =
        new Transaction(
            BigDecimal.TEN,
            ZonedDateTime.of(LocalDateTime.of(2021, 5, 2, 10, 20, 0), ZoneOffset.UTC));

    Transaction transaction2 =
        new Transaction(
            BigDecimal.ONE,
            ZonedDateTime.of(LocalDateTime.of(2021, 5, 2, 13, 5, 0), ZoneOffset.UTC));
    transactionRepo.addTransaction(transaction1);
    transactionRepo.addTransaction(transaction2);
    transactionRepo.deleteAll();

    List<Transaction> transactionsInATimeRange =
        transactionRepo.getTransactionsInATimeRange(
            ZonedDateTime.of(2021, 5, 2, 9, 30, 0, 0, ZoneId.of("UTC")),
            ZonedDateTime.of(2021, 5, 2, 19, 30, 0, 0, ZoneId.of("UTC")));

    assertEquals(0, transactionsInATimeRange.size());
  }
}
