package com.n26.transactions.model;

import java.math.BigDecimal;

public class Statistics {
  private BigDecimal sum;

  public BigDecimal getSum() {
    return sum;
  }

  public Statistics(BigDecimal sum, BigDecimal avg, Long count, BigDecimal max, BigDecimal min) {
    this.sum = sum;
    this.avg = avg;
    this.count = count;
    this.max = max;
    this.min = min;
  }

  public void setSum(BigDecimal sum) {
    this.sum = sum;
  }

  public BigDecimal getAvg() {
    return avg;
  }

  public void setAvg(BigDecimal avg) {
    this.avg = avg;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public BigDecimal getMax() {
    return max;
  }

  public void setMax(BigDecimal max) {
    this.max = max;
  }

  public BigDecimal getMin() {
    return min;
  }

  public void setMin(BigDecimal min) {
    this.min = min;
  }

  private BigDecimal avg;
  private Long count;
  private BigDecimal max;
  private BigDecimal min;
}
