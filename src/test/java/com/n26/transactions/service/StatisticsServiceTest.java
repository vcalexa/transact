package com.n26.transactions.service;

import static java.math.RoundingMode.HALF_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.n26.transactions.model.Statistics;
import com.n26.transactions.model.Transaction;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

  @InjectMocks private StatisticsService statisticsService;
  @Mock private TransactionService transactionService;

  private static final BigDecimal TRANSACTION_VALUE1 = BigDecimal.valueOf(1250.52);
  private static final BigDecimal TRANSACTION_VALUE2 = BigDecimal.valueOf(250.2);
  private static final BigDecimal TRANSACTION_VALUE3 = BigDecimal.valueOf(70.52);

  @Test
  public void getEmptyStatistics() {
    when(transactionService.getTransactions()).thenReturn(Collections.emptyList());

    final Statistics result = statisticsService.getStatistics();

    assertEquals((long) result.getCount(), 0);
    assertEquals(BigDecimal.valueOf(0), result.getAvg());
    assertEquals(BigDecimal.valueOf(0), result.getMax());
    assertEquals(BigDecimal.valueOf(0), result.getMin());
    assertEquals(BigDecimal.valueOf(0), result.getSum());
  }

  @Test
  public void getStatisticsFromValidTransactions() {
    Transaction t1 = new Transaction(TRANSACTION_VALUE1, Instant.now());
    Transaction t2 = new Transaction(TRANSACTION_VALUE2, Instant.now());
    Transaction t3 = new Transaction(TRANSACTION_VALUE3, Instant.now());
    when(transactionService.getTransactions()).thenReturn(List.of(t1, t2, t3));

    final Statistics result = statisticsService.getStatistics();

    assertEquals((long) result.getCount(), 3);
    assertEquals(BigDecimal.valueOf(523.75), result.getAvg());
    assertEquals(BigDecimal.valueOf(1250.52), result.getMax());
    assertEquals(BigDecimal.valueOf(70.52), result.getMin());
    assertEquals(BigDecimal.valueOf(1571.24), result.getSum());
  }

  @Test
  public void getStatisticsRounding() {
    Transaction t1 = new Transaction(BigDecimal.valueOf(10.345), Instant.now());
    Transaction t2 = new Transaction(BigDecimal.valueOf(10.345), Instant.now());
    when(transactionService.getTransactions()).thenReturn(List.of(t1, t2));

    final Statistics result = statisticsService.getStatistics();

    assertEquals((long) result.getCount(), 2);
    assertEquals(BigDecimal.valueOf(10.35), result.getAvg());
    assertEquals(BigDecimal.valueOf(10.35), result.getMax());
    assertEquals(BigDecimal.valueOf(10.35), result.getMin());
    assertEquals(BigDecimal.valueOf(20.70).setScale(2, HALF_UP), result.getSum());
  }
}
