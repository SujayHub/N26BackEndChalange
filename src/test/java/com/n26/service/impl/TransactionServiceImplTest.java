package com.n26.service.impl;

import com.n26.domain.Transaction;
import com.n26.repo.TransactionRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

  @Mock private TransactionRepo transactionRepo;
  private TransactionServiceImpl transactionService;

  @Before
  public void setUp() throws Exception {
    Clock clock = Clock.fixed(Instant.parse("2021-05-02T10:20:30.000Z"), ZoneId.of("UTC"));
    transactionService = new TransactionServiceImpl(transactionRepo, clock);
  }

  @Test
  public void addTransactionShouldReturnTrueWhenTransactionTimeStampIsWithIn60SecRange() {
    Transaction transaction =
        new Transaction(
            BigDecimal.TEN,
            ZonedDateTime.ofInstant(Instant.parse("2021-05-02T10:20:35.000Z"), ZoneId.of("UTC")));
    doNothing().when(transactionRepo).addTransaction(transaction);
    assertTrue(transactionService.addTransaction(transaction));
    verify(transactionRepo, atLeastOnce()).addTransaction(transaction);
  }

  @Test
  public void addTransactionShouldReturnFalseWhenTransactionTimeStampIsWithIn60SecRange() {
    Transaction transaction =
        new Transaction(
            BigDecimal.TEN,
            ZonedDateTime.ofInstant(Instant.parse("2021-05-02T10:19:25.000Z"), ZoneId.of("UTC")));
    assertFalse(transactionService.addTransaction(transaction));
    verifyNoMoreInteractions(transactionRepo);
  }

  @Test
  public void getTransactionsInATimeRangeShouldReturnANonEmptyListOfObjects() {
    Transaction transaction =
        new Transaction(
            BigDecimal.TEN,
            ZonedDateTime.ofInstant(Instant.parse("2021-05-02T10:20:35.000Z"), ZoneId.of("UTC")));
    ZonedDateTime startTime =
        ZonedDateTime.ofInstant(Instant.parse("2021-05-02T10:20:35.000Z"), ZoneId.of("UTC"));
    ZonedDateTime endTime =
        ZonedDateTime.ofInstant(Instant.parse("2021-05-02T10:21:35.000Z"), ZoneId.of("UTC"));

    doReturn(singletonList(transaction))
        .when(transactionRepo)
        .getTransactionsInATimeRange(startTime, endTime);
    List<Transaction> transactionsInLast60Sec =
        transactionService.getTransactionsInATimeRange(startTime, endTime);

    assertEquals(singletonList(transaction), transactionsInLast60Sec);
    verify(transactionRepo, atLeastOnce()).getTransactionsInATimeRange(startTime, endTime);
  }

  @Test
  public void getTransactionsInATimeRangeShouldReturnAEmptyList() {
    ZonedDateTime startTime =
        ZonedDateTime.ofInstant(Instant.parse("2021-05-02T10:20:35.000Z"), ZoneId.of("UTC"));
    ZonedDateTime endTime =
        ZonedDateTime.ofInstant(Instant.parse("2021-05-02T10:21:35.000Z"), ZoneId.of("UTC"));

    doReturn(emptyList()).when(transactionRepo).getTransactionsInATimeRange(startTime, endTime);
    List<Transaction> transactionsInLast60Sec =
        transactionService.getTransactionsInATimeRange(startTime, endTime);

    assertEquals(emptyList(), transactionsInLast60Sec);
    verify(transactionRepo, atLeastOnce()).getTransactionsInATimeRange(startTime, endTime);
  }

  @Test
  public void deleteTransactionsShouldDeleteAllTheTransactions() {
    ZonedDateTime startTime =
        ZonedDateTime.ofInstant(Instant.parse("2021-05-02T10:20:35.000Z"), ZoneId.of("UTC"));
    ZonedDateTime endTime =
        ZonedDateTime.ofInstant(Instant.parse("2021-05-02T10:21:35.000Z"), ZoneId.of("UTC"));
    transactionService.deleteTransactions();
    List<Transaction> transactionInLast60Sec =
        transactionService.getTransactionsInATimeRange(startTime, endTime);

    assertEquals(emptyList(), transactionInLast60Sec);
    verify(transactionRepo, atLeastOnce()).deleteAll();
    verify(transactionRepo, atLeastOnce()).getTransactionsInATimeRange(startTime, endTime);
  }
}
