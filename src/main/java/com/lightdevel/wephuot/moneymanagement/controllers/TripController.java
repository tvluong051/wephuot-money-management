package com.lightdevel.wephuot.moneymanagement.controllers;

import com.lightdevel.wephuot.moneymanagement.models.entities.User;
import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;
import com.lightdevel.wephuot.moneymanagement.models.out.TripOut;
import com.lightdevel.wephuot.moneymanagement.services.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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

    /* Get informations about trip */
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

    /* Create, update details for trip */
    @PostMapping(value = "/trip")
    public String saveTrip(@RequestBody @Valid TripIn trip) {
        LOGGER.info("POST - Save trip: {}", trip);
        return this.tripService.save(trip);
    }

    @PostMapping(value = "/trip/{tripId}/participants")
    public ResponseEntity<String> addTripParticipants(@PathVariable("tripId") String tripId, @RequestBody List<User> participants) {
        String resultId = this.tripService.addParticipants(tripId, participants);
        if( resultId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(resultId);
    }

    /* Validate or delete trip */
    @PutMapping(value = "/trip/{tripId}")
    public ResponseEntity<String> validateTrip(@PathVariable("tripId") String tripId) {
        String resultId = this.tripService.validateTrip(tripId);
        if( resultId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(resultId);
    }

}
