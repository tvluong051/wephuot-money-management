package com.lightdevel.wephuot.moneymanagement.controllers;

import com.lightdevel.wephuot.moneymanagement.models.in.SpendingIn;
import com.lightdevel.wephuot.moneymanagement.models.out.SpendingOut;
import com.lightdevel.wephuot.moneymanagement.services.SpendingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@Api("Manipulation spendings")
@RestController
@RequestMapping("/api/v1/spendings")
public class SpendingController {

    private SpendingService spendingService;

    @Autowired
    public SpendingController(SpendingService spendingService) {
        this.spendingService = Objects.requireNonNull(spendingService);
    }

    /* Get all spendings info */
    @ApiOperation("Get all spendings of trip using trip id")
    @GetMapping
    public List<SpendingOut> getAllSpendingsOfTrip(@RequestParam("tripId") String tripId) {
        log.info("GET - Get all spendings of trip = {}", tripId);
        return this.spendingService.getAllSpendingsOfTrip(tripId);
    }

    /* Create new spending */
    @ApiOperation("Add new spending to trip")
    @PostMapping("/spending")
    public SpendingOut createSpending(@RequestParam("tripId") String tripId,
                                      @RequestBody SpendingIn spendingIn) {
        log.info("POST - Create new spending = {} for trip id = {}", spendingIn, tripId);
        return this.spendingService.addSpending(tripId, spendingIn);
    }


    /* Update spending */
    @ApiOperation("Update spending details")
    @PutMapping("/spending/{spendingId}")
    public SpendingOut updateSpending(@PathVariable("spendingId") Long spendingId,
                                      @RequestBody SpendingIn spendingIn) {
        log.info("PUT - Update spending id = {}", spendingId);
        return this.spendingService.updateSpending(spendingId, spendingIn);
    }

    /* Delete spending */
    @ApiOperation("Delete spending")
    @DeleteMapping("/spending/{spendingId}")
    public ResponseEntity<Void> deleteSpending(@PathVariable("spendingId") Long spendingId) {
        log.info("DELETE - delete spending id = {}", spendingId);
        this.spendingService.deleteSpending(spendingId);
        return ResponseEntity.ok().build();
    }
}
