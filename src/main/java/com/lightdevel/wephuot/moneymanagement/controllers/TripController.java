package com.lightdevel.wephuot.moneymanagement.controllers;

import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;
import com.lightdevel.wephuot.moneymanagement.services.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripController.class);

    private TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = Objects.requireNonNull(tripService);
    }

    @PostMapping(value = "/trip")
    public String saveTrip(@RequestBody @Valid TripIn trip) {
        LOGGER.info("Request save trip: {}", trip);
        return this.tripService.save(trip);
    }

    // @PutMapping(value = "/trip/{tripId}")
}
