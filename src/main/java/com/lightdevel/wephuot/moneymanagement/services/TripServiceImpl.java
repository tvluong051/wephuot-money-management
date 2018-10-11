package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.models.entities.Trip;
import com.lightdevel.wephuot.moneymanagement.models.enums.TripStatus;
import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;
import com.lightdevel.wephuot.moneymanagement.repositories.TripRepository;
import com.lightdevel.wephuot.moneymanagement.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    private TripRepository tripRepository;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository) {
        this.tripRepository = Objects.requireNonNull(tripRepository);
    }

    @Override
    public String save(TripIn trip) {
        if (trip.getTripId() == null) {
            Trip newTrip = new Trip(Util.generatedUUID(), trip.getName(), trip.getDescription(), TripStatus.PENDING);
             return this.tripRepository.save(newTrip).getTripId();
        }
        Optional<Trip> optionalExistingTrip = this.tripRepository.findById(trip.getTripId());
        if (!optionalExistingTrip.isPresent()) {
            throw new RuntimeException();
        }
        Trip existingTrip = optionalExistingTrip.get();
        if (TripStatus.VALIDATED.equals(existingTrip.getStatus())) {
            throw new RuntimeException();
        }
        existingTrip.setName(trip.getName());
        existingTrip.setDescription(trip.getDescription());
        return this.tripRepository.save(existingTrip).getTripId();
    }

}
