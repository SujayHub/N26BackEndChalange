package com.n26.controller.rest;

import static java.math.RoundingMode.HALF_UP;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.n26.domain.Statistics;
import com.n26.service.StatisticsService;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

  @Autowired MockMvc mockMvc;

  @MockBean private StatisticsService statisticsService;

  private static Statistics getMockStatistics() {
    Statistics statistics = new Statistics();
    statistics.setSum(BigDecimal.valueOf(1000));
    statistics.setAvg(BigDecimal.valueOf(100.53));
    statistics.setMax(BigDecimal.valueOf(200000.49));
    statistics.setMin(BigDecimal.valueOf(50.23));
    statistics.setCount(10);
    return statistics.withScale(2, HALF_UP);
  }

  @Test
  public void getStatisticsShouldReturnStatusOk() throws Exception {
    doReturn(getMockStatistics()).when(statisticsService).getStatisticsRounded();
    this.mockMvc
        .perform(get("/statistics").contentType(APPLICATION_JSON_UTF8))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.avg", is("100.53")))
        .andExpect(jsonPath("$.min", is("50.23")))
        .andExpect(jsonPath("$.max", is("200000.49")))
        .andExpect(jsonPath("$.count", is(10)))
        .andExpect(jsonPath("$.sum", is("1000.00")));
  }
}
