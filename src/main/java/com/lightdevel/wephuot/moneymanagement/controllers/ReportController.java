package com.lightdevel.wephuot.moneymanagement.controllers;

import com.lightdevel.wephuot.moneymanagement.models.out.BalanceOut;
import com.lightdevel.wephuot.moneymanagement.services.ReportService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Api("Get reports details")
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

  private ReportService reportService;

  @Autowired
  public ReportController(ReportService reportService) {
    this.reportService = Objects.requireNonNull(reportService);
  }

  @GetMapping("/balance/{tripId}")
  public BalanceOut getTripBalanceReport(@PathVariable("tripId") String tripId) {
    LOGGER.info("GET - Get balance report for trip id = {}", tripId);
    return this.reportService.getBalance(tripId);
  }
}
