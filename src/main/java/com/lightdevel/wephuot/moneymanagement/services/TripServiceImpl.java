package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.models.entities.Participant;
import com.lightdevel.wephuot.moneymanagement.models.entities.Trip;
import com.lightdevel.wephuot.moneymanagement.models.enums.TripStatus;
import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;
import com.lightdevel.wephuot.moneymanagement.models.out.TripOut;
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
    public Set<TripOut> getAllTripsOfUser(String userId) {
        List<Participant> participants = this.participantRepository.findAllByUserUserId(userId);
        if(participants != null && participants.size() > 0) {
            return participants.stream()
                    .map(participant -> {
                        Trip trip = participant.getTrip();
                        return TripOut.builder()
                                .tripId(trip.getTripId())
                                .name(trip.getName())
                                .description(trip.getDescription())
                                .status(trip.getStatus())
                                .createdDate(trip.getCreatedDate())
                                .lastModified(trip.getLastModified())
                                .build();
                    })
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    @Override
    public TripOut getDetail(String tripId) {
        TripOut.TripOutBuilder tripOutBuilder = TripOut.builder();
        List<Participant> participants = this.participantRepository.findAllByTripTripId(tripId);
        if(participants == null || participants.size() == 0) {
            return tripOutBuilder.build();
        }
        Trip trip = participants.get(0).getTrip();
        return tripOutBuilder
                .tripId(trip.getTripId())
                .name(trip.getName())
                .description(trip.getDescription())
                .status(trip.getStatus())
                .createdDate(trip.getCreatedDate())
                .lastModified(trip.getLastModified())
                .participants(participants.stream()
                        .map(Participant::getUser)
                        .collect(Collectors.toSet()))
                .build();
    }


}
