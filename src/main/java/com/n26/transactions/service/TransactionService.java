package com.n26.transactions.service;

import com.n26.transactions.model.Transaction;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  private List<Transaction> transactionList;
  private static final long EXPIRY_TIME = 60000;

  public TransactionService() {
    transactionList = Collections.synchronizedList(new ArrayList<>());
  }

  public void addTransaction(Transaction transaction) {
    if (isActive(transaction, EXPIRY_TIME)) transactionList.add(transaction);
  }

  public void deleteAllTransactions() {
    transactionList.clear();
  }

  public boolean isActiveLast60Seconds(Transaction transaction) {
    return isActive(transaction, EXPIRY_TIME);
  }

  public boolean isActive(Transaction transaction, long expirationTimeInMillis) {

    if (transaction == null) return false;
    return getAgeToMillis(transaction) < expirationTimeInMillis;
  }

  public boolean isFuture(Transaction transaction) {
    if (transaction == null) return true;
    return getAgeToMillis(transaction) < 0;
  }

  public boolean isInvalid(Transaction transaction) {
    return transaction == null
        || transaction.getAmount() == null
        || transaction.getTimestamp() == null;
  }

  public List<Transaction> getTransactions() {
    return transactionList.stream()
        .filter(this::isActiveLast60Seconds)
        .collect(Collectors.toList());
  }

  private long getAgeToMillis(Transaction transaction) {
    Duration transactionAge = Duration.between(transaction.getTimestamp(), Instant.now());
    return transactionAge.toMillis();
  }
}
