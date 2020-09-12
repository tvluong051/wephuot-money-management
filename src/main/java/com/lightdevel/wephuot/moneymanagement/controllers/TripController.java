package com.lightdevel.wephuot.moneymanagement.controllers;

import com.lightdevel.wephuot.moneymanagement.models.entities.User;
import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;
import com.lightdevel.wephuot.moneymanagement.models.out.TripOut;
import com.lightdevel.wephuot.moneymanagement.services.TripService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Api("Manipulation trips")
@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    private TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = Objects.requireNonNull(tripService);
    }

    /* Get informations about trip */
    @ApiOperation("Get all trip of user")
    @GetMapping
    public Set<TripOut> getAllUserTrips(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        log.info("GET - Get all trips of user = {}", userId);
        return this.tripService.getAllTripsOfUser(userId);
    }

    @ApiOperation("Get trip details")
    @GetMapping(value = "/trip/{tripId}")
    public TripOut getTripDetail(@PathVariable("tripId") String tripId, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        log.info("GET - Get detail of trip with id = {} from user = {}", tripId, userId);
        return this.tripService.getDetail(tripId, userId);
    }

    /* Create, update details for trip */
    @ApiOperation(value = "Create new trip or update existing one", notes = "If new trip is created, it has PENDING state, waiting for validation. If trip exists, we can only update it when it has PENDING state")
    @PostMapping(value = "/trip")
    public String saveTrip(@RequestBody @Valid TripIn trip) {
        log.info("POST - Save trip: {}", trip);
        return this.tripService.save(trip);
    }

    @ApiOperation("Add participants to trip")
    @PostMapping(value = "/trip/{tripId}/participants")
    public ResponseEntity<String> addTripParticipants(@PathVariable("tripId") String tripId, @RequestBody List<User> participants) {
        String resultId = this.tripService.addParticipants(tripId, participants);
        if( resultId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(resultId);
    }

    /* Validate or delete trip */
    @ApiOperation("Validate PENDING trip")
    @PutMapping(value = "/trip/{tripId}")
    public ResponseEntity<String> validateTrip(@PathVariable("tripId") String tripId, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        log.info("PUT - Validate trip with id = {} from user = {}", tripId, userId);
        String resultId = this.tripService.validateTrip(tripId, userId);
        if( resultId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(resultId);
    }

}
