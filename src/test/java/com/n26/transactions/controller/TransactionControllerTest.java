package com.n26.transactions.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.transactions.model.Transaction;
import com.n26.transactions.service.TransactionService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

  @InjectMocks private TransactionController transactionController;
  @MockBean private TransactionService transactionService;

  @Autowired private MockMvc mockMvc;

  @Test
  public void getValidTransactionsExpect200() throws Exception {

    Transaction t1 = new Transaction(BigDecimal.valueOf(10.345), Instant.now());
    Transaction t2 = new Transaction(BigDecimal.valueOf(12.345), Instant.now());
    when(transactionService.getTransactions()).thenReturn(List.of(t1, t2));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/transactions").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].amount", Matchers.is(10.345)))
        .andExpect(jsonPath("$.[1].amount", Matchers.is(12.345)));
  }

  @Test
  public void postValidTransactionExpect201() throws Exception {
    when(transactionService.isFuture(any())).thenReturn(false);
    when(transactionService.isInvalid(any())).thenReturn(false);
    when(transactionService.isActiveLast60Seconds(any())).thenReturn(true);
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    ("{\n"
                        + "\"amount\":\"123.22\",\n"
                        + "\"timestamp\":\"2022-06-19T15:39:00.00Z\"\n"
                        + "}")))
        .andExpect(status().is(201));
  }

  @Test
  public void postFutureTransactionsExpect422() throws Exception {
    when(transactionService.isFuture(any())).thenReturn(true);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    ("{\n"
                        + "\"amount\":\"123.22\",\n"
                        + "\"timestamp\":\"2022-06-19T15:39:00.00Z\"\n"
                        + "}")))
        .andExpect(status().is(422));
  }

  @Test
  public void postInvalidTransactionsExpect422() throws Exception {
    Transaction t1 = new Transaction(BigDecimal.valueOf(10.345), Instant.now());
    ObjectMapper objectMapper = new ObjectMapper();
    when(transactionService.isInvalid(any())).thenReturn(true);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(t1)))
        .andExpect(status().is(422));
  }
}
