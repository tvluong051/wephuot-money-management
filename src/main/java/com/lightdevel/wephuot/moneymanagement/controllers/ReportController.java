package com.lightdevel.wephuot.moneymanagement.controllers;

import com.lightdevel.wephuot.moneymanagement.models.out.BalanceOut;
import com.lightdevel.wephuot.moneymanagement.services.ReportService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Api("Get reports details")
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

  private ReportService reportService;

  @Autowired
  public ReportController(ReportService reportService) {
    this.reportService = Objects.requireNonNull(reportService);
  }

  @GetMapping("/balance/{tripId}")
  public BalanceOut getTripBalanceReport(@PathVariable("tripId") String tripId, HttpServletRequest request) {
    String userId = (String) request.getAttribute("userId");
    log.info("GET - Get balance report for trip id = {} from user = {}", tripId, userId);
    return this.reportService.getBalance(tripId, userId);
  }
}
