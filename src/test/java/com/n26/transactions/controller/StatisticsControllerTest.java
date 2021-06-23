package com.n26.transactions.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.n26.transactions.model.Statistics;
import com.n26.transactions.service.StatisticsService;
import java.math.BigDecimal;
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
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

  @InjectMocks private StatisticsController statisticsController;
  @MockBean private StatisticsService statisticsService;

  @Autowired private MockMvc mockMvc;

  @Test
  public void getStatisticsExpect200AndCorrectValues() throws Exception {

    Statistics s1 =
        new Statistics(
            BigDecimal.valueOf(2058.25),
            BigDecimal.valueOf(1558.25),
            3L,
            BigDecimal.valueOf(758.25),
            BigDecimal.valueOf(358.25));
    when(statisticsService.getStatistics()).thenReturn(s1);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/statistics").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sum", Matchers.is(2058.25)))
        .andExpect(jsonPath("$.avg", Matchers.is(1558.25)))
        .andExpect(jsonPath("$.count", Matchers.is(3)))
        .andExpect(jsonPath("$.max", Matchers.is(758.25)))
        .andExpect(jsonPath("$.min", Matchers.is(358.25)));
  }

  @Test
  public void emptyStatisticsExpect200AndZeroValues() throws Exception {
    Statistics s1 =
        new Statistics(
            BigDecimal.valueOf(0),
            BigDecimal.valueOf(0),
            0L,
            BigDecimal.valueOf(0),
            BigDecimal.valueOf(0));
    when(statisticsService.getStatistics()).thenReturn(s1);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/statistics").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sum", Matchers.is(0)))
        .andExpect(jsonPath("$.avg", Matchers.is(0)))
        .andExpect(jsonPath("$.count", Matchers.is(0)))
        .andExpect(jsonPath("$.max", Matchers.is(0)))
        .andExpect(jsonPath("$.min", Matchers.is(0)));
  }
}
