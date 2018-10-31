package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.models.entities.Participant;
import com.lightdevel.wephuot.moneymanagement.models.entities.Trip;
import com.lightdevel.wephuot.moneymanagement.models.enums.TripStatus;
import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;
import com.lightdevel.wephuot.moneymanagement.repositories.ParticipantRepository;
import com.lightdevel.wephuot.moneymanagement.repositories.TripRepository;
import com.lightdevel.wephuot.moneymanagement.utils.Util;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    private ParticipantRepository participantRepository;
    private TripRepository tripRepository;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository,
                           ParticipantRepository participantRepository) {
        this.tripRepository = Objects.requireNonNull(tripRepository);
        this.participantRepository = Objects.requireNonNull(participantRepository);
    }

    @Override
    public String save(TripIn trip) {
        long now = DateTime.now().getMillis();

        if (trip.getTripId() == null) {
            Trip newTrip = new Trip(Util.generatedUUID(), trip.getName(), trip.getDescription(), TripStatus.PENDING);
            newTrip.setCreatedDate(now);
            newTrip.setLastModified(now);
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
        existingTrip.setLastModified(now);
        return this.tripRepository.save(existingTrip).getTripId();
    }

    @Override
    public Set<Trip> getAllTripsOfUser(String userId) {
        List<Participant> userTrip = this.participantRepository.findAllByUserUserId(userId);
        if(userTrip != null && userTrip.size() > 0) {
            return userTrip.stream().map(Participant::getTrip).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }


}
