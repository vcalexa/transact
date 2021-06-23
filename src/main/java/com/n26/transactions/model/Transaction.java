package com.n26.transactions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;

public class Transaction {
  private BigDecimal amount;
  private Instant timestamp;

  public Transaction(
      @JsonProperty(required = true) BigDecimal amount,
      @JsonProperty(required = true) Instant timestamp) {
    this.amount = amount;
    this.timestamp = timestamp;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  public Instant getTimestamp() {
    return timestamp;
  }
}
