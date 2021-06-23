package com.n26.transactions.service;

import static org.junit.jupiter.api.Assertions.*;

import com.n26.transactions.model.Transaction;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class TransactionServiceTest extends Thread {
  private static final BigDecimal TRANSACTION_VALUE = BigDecimal.valueOf(1250.52);
  TransactionService transactionService = new TransactionService();

  @Test
  public void transactionExpiration() throws InterruptedException {
    Transaction t1 = new Transaction(TRANSACTION_VALUE, Instant.now());

    Thread.sleep(2000);
    assertTrue(transactionService.isActive(t1, 3000));

    Thread.sleep(1000);
    assertFalse(transactionService.isActive(t1, 3000));
  }

  @Test
  public void transactionInvalid() {
    Transaction t = new Transaction(null, null);

    assertTrue(transactionService.isInvalid(t));
    assertTrue(transactionService.isInvalid(null));
  }

  @Test
  public void futureTransaction() {
    Transaction t1 = new Transaction(TRANSACTION_VALUE, Instant.now().plusSeconds(10));

    assertTrue(transactionService.isFuture(t1));
  }

  @Test
  public void futureTransaction2() {
    Transaction t1 = new Transaction(TRANSACTION_VALUE, Instant.parse("2035-06-19T15:39:00.00Z"));

    assertTrue(transactionService.isFuture(t1));
  }

  @Test
  public void deleteTransaction() {
    Transaction t1 = new Transaction(TRANSACTION_VALUE, Instant.now());

    transactionService.addTransaction(t1);
    assertEquals(1, transactionService.getTransactions().size());

    transactionService.deleteAllTransactions();
    assertEquals(0, transactionService.getTransactions().size());
  }

  @Test
  public void addConcurrentTransactions() throws InterruptedException {
    final Transaction TEST_TRANSACTION = new Transaction(TRANSACTION_VALUE, Instant.now());
    Thread[] threadArray = new Thread[16];
    for (int i = 0; i < 16; i++) {
      threadArray[i] = new Thread(() -> transactionService.addTransaction(TEST_TRANSACTION));
      threadArray[i].start();
      threadArray[i].join();
    }
    assertEquals(16, transactionService.getTransactions().size());
  }
}
