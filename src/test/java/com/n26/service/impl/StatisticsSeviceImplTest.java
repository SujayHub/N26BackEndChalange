package com.n26.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.n26.domain.Statistics;
import com.n26.domain.Transaction;
import com.n26.service.TransactionService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsSeviceImplTest {

  @Mock TransactionService transactionService;
  private StatisticsSeviceImpl statisticsService;
  private Clock clock;

  @Before
  public void setUp() {
    clock = Clock.fixed(Instant.parse("2021-05-02T10:20:30.000Z"), ZoneId.of("UTC"));
    statisticsService = new StatisticsSeviceImpl(transactionService, clock);
  }

  @Test
  public void shouldFindStatistics() {
    ZonedDateTime currentTime = ZonedDateTime.now(clock);
    ZonedDateTime someRandomTimeStamp =
        ZonedDateTime.of(LocalDateTime.of(1995, 5, 2, 10, 20, 0), ZoneOffset.UTC);
    when(transactionService.getTransactionsInATimeRange(currentTime.minusSeconds(60L), currentTime))
        .thenReturn(
            Arrays.asList(
                new Transaction(BigDecimal.valueOf(9.0), someRandomTimeStamp),
                new Transaction(BigDecimal.TEN, someRandomTimeStamp),
                new Transaction(BigDecimal.ONE, someRandomTimeStamp)));
    Statistics expectedStatistics =
        new Statistics(
            roundedBigDecimal(6.67),
            roundedBigDecimal(1.00),
            roundedBigDecimal(10.00),
            3,
            roundedBigDecimal(20.00));

    Statistics statistics = statisticsService.getStatisticsRounded();
    assertEquals(expectedStatistics, statistics);
  }

  private BigDecimal roundedBigDecimal(double val) {
    return BigDecimal.valueOf(val).setScale(2, RoundingMode.HALF_UP);
  }
}
