package com.n26.service.impl;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;

import com.n26.domain.Statistics;
import com.n26.domain.Transaction;
import com.n26.service.StatisticsService;
import com.n26.service.TransactionService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticsSeviceImpl implements StatisticsService {

  private final TransactionService transactionService;
  private final Clock clock;

  @Override
  public Statistics getStatisticsRounded() {
    return getStatistics().withScale(2, HALF_UP);
  }

  private Statistics getStatistics() {
    ZonedDateTime now = ZonedDateTime.now(clock);
    List<BigDecimal> transactionAmountInLast60secs =
        transactionService.getTransactionsInATimeRange(now.minusSeconds(60L), now).stream()
            .map(Transaction::getAmount)
            .collect(Collectors.toList());

    long count = transactionAmountInLast60secs.size();
    BigDecimal min = transactionAmountInLast60secs.stream().min(comparing(identity())).orElse(ZERO);
    BigDecimal max = transactionAmountInLast60secs.stream().max(comparing(identity())).orElse(ZERO);
    BigDecimal sum = transactionAmountInLast60secs.stream().reduce(BigDecimal::add).orElse(ZERO);
    BigDecimal average = count == 0L ? ZERO : sum.divide(BigDecimal.valueOf(count), 14, HALF_UP);

    return new Statistics(average, min, max, count, sum);
  }
}
