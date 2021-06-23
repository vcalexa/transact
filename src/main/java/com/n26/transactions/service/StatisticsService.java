package com.n26.transactions.service;

import static java.math.RoundingMode.HALF_UP;

import com.n26.transactions.model.Statistics;
import com.n26.transactions.model.Transaction;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {
  @Autowired private TransactionService transactionService;

  public Statistics getStatistics() {
    List<Transaction> transactionList = transactionService.getTransactions();
    long numberOfTransactions = transactionList.size();

    BigDecimal sum =
        transactionList.stream()
            .map(trans -> trans.getAmount().setScale(2, HALF_UP))
            .reduce(BigDecimal.ZERO, (i, j) -> i.add(j));

    BigDecimal avg = BigDecimal.ZERO;

    if (numberOfTransactions != 0) {
      avg = sum.divide(BigDecimal.valueOf(numberOfTransactions), HALF_UP);
    }

    BigDecimal min =
        transactionList.stream()
            .map(Transaction::getAmount)
            .min(Comparator.naturalOrder())
            .map(res -> res.setScale(2, HALF_UP))
            .orElse(BigDecimal.ZERO);
    BigDecimal max =
        transactionList.stream()
            .map(Transaction::getAmount)
            .max(Comparator.naturalOrder())
            .map(res -> res.setScale(2, HALF_UP))
            .orElse(BigDecimal.ZERO);

    return new Statistics(sum, avg, numberOfTransactions, max, min);
  }
}
