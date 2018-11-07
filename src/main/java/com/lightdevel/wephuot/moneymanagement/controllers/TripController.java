package com.lightdevel.wephuot.moneymanagement.controllers;

import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;
import com.lightdevel.wephuot.moneymanagement.models.out.TripOut;
import com.lightdevel.wephuot.moneymanagement.services.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Set;

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
        LOGGER.info("POST - Save trip: {}", trip);
        return this.tripService.save(trip);
    }

    @GetMapping
    public Set<TripOut> getAllUserTrips(@RequestParam("userId") String userId) {
        LOGGER.info("GET - Get all trips of user = {}", userId);
        return this.tripService.getAllTripsOfUser(userId);
    }

    @GetMapping(value = "/trip/{tripId}")
    public TripOut getTripDetail(@PathVariable("tripId") String tripId) {
        LOGGER.info("GET - Get detail of trip with id = {}", tripId);
        return this.tripService.getDetail(tripId);
    }

    // @PutMapping(value = "/trip/{tripId}")
}
