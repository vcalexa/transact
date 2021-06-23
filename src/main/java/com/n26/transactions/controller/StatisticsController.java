package com.n26.transactions.controller;

import com.n26.transactions.model.Statistics;
import com.n26.transactions.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

  @Autowired private StatisticsService statisticsService;

  @RequestMapping(value = "/statistics", method = RequestMethod.GET)
  public ResponseEntity<Statistics> newTransaction() {
    Statistics statistics = statisticsService.getStatistics();

    return new ResponseEntity<>(statistics, HttpStatus.OK);
  }
}
