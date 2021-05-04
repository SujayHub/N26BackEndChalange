package com.n26.controller.rest;

import com.n26.domain.Statistics;
import com.n26.service.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class StatisticsController {
  private final StatisticsService statisticsService;

  @GetMapping
  public Statistics getStatistics() {
    return statisticsService.getStatisticsRounded();
  }
}
