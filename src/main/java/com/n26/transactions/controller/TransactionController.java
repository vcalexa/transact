package com.n26.transactions.controller;

import com.n26.transactions.model.Transaction;
import com.n26.transactions.service.TransactionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

  @Autowired private TransactionService transactionService;

  @RequestMapping(value = "/transactions", method = RequestMethod.POST)
  public ResponseEntity<Transaction> newTransaction(@RequestBody Transaction transaction) {

      return transactionService.addTransaction2(transaction);

  }

  @RequestMapping(value = "/transactions", method = RequestMethod.GET)
  public ResponseEntity<List<Transaction>> getTransactions() {

    return new ResponseEntity<>(transactionService.getTransactions(), HttpStatus.OK);
  }

  @RequestMapping(value = "/transactions", method = RequestMethod.DELETE)
  public ResponseEntity<String> deleteTransactions() {
    transactionService.deleteAllTransactions();
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
