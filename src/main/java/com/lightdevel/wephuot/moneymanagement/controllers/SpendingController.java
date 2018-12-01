package com.lightdevel.wephuot.moneymanagement.controllers;

import com.lightdevel.wephuot.moneymanagement.models.out.SpendingOut;
import com.lightdevel.wephuot.moneymanagement.services.SpendingService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Api("Manipulation spendings")
@RestController
@RequestMapping("/api/v1/spendings")
public class SpendingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpendingController.class);

    private SpendingService spendingService;

    @Autowired
    public SpendingController(SpendingService spendingService) {
        this.spendingService = Objects.requireNonNull(spendingService);
    }

    /* Get all spendings info */
    @GetMapping
    public List<SpendingOut> getAllSpendingsOfTrip(@RequestParam("tripId") String tripId) {
        LOGGER.info("GET - Get all spendings or trip = {}", tripId);
        return this.spendingService.getAllSpendingsOfTrip(tripId);
    }
}
