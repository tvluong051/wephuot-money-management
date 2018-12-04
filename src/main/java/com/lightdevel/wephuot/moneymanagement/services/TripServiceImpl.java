package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.exceptions.BusinessException;
import com.lightdevel.wephuot.moneymanagement.models.entities.Participant;
import com.lightdevel.wephuot.moneymanagement.models.entities.Trip;
import com.lightdevel.wephuot.moneymanagement.models.entities.User;
import com.lightdevel.wephuot.moneymanagement.models.enums.TripStatus;
import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;
import com.lightdevel.wephuot.moneymanagement.models.out.TripOut;
import com.lightdevel.wephuot.moneymanagement.repositories.ParticipantRepository;
import com.lightdevel.wephuot.moneymanagement.repositories.TripRepository;
import com.lightdevel.wephuot.moneymanagement.repositories.UserRepository;
import com.lightdevel.wephuot.moneymanagement.utils.FileUtil;
import com.lightdevel.wephuot.moneymanagement.utils.Util;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    private ParticipantRepository participantRepository;
    private TripRepository tripRepository;
    private UserRepository userRepository;
    private FileUtil fileUtil;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository,
                           ParticipantRepository participantRepository,
                           UserRepository userRepository,
                           FileUtil fileUtil) {
        this.tripRepository = Objects.requireNonNull(tripRepository);
        this.participantRepository = Objects.requireNonNull(participantRepository);
        this.userRepository = Objects.requireNonNull(userRepository);
        this.fileUtil = Objects.requireNonNull(fileUtil);
    }

    @Override
    public String save(TripIn trip) {
        Trip createdTrip = saveTripDetail(trip);
        if(!StringUtils.isEmpty(trip.getCoverPhotoBase64Encoded())) {
            // TODO: maybe can be optimized if we decide not to change file name once saved
            createdTrip.setCoverPhoto(fileUtil.saveCoverPhoto(createdTrip.getTripId(), trip.getCoverPhotoBase64Encoded()));
            this.tripRepository.save(createdTrip);
        }
        return createdTrip.getTripId();
    }

    Trip saveTripDetail(TripIn trip) {
        long now = DateTime.now().getMillis();

        if (trip.getTripId() == null) {
            Trip newTrip = new Trip(Util.generatedUUID(), trip.getName(), trip.getDescription(), TripStatus.PENDING, null);
            newTrip.setCreatedDate(now);
            newTrip.setLastModified(now);
            return this.tripRepository.save(newTrip);
        }
        Optional<Trip> optionalExistingTrip = this.tripRepository.findById(trip.getTripId());
        if (!optionalExistingTrip.isPresent()) {
            throw new BusinessException("Trip id = " + trip.getTripId() + " doesn't exist");
        }
        Trip existingTrip = optionalExistingTrip.get();
        if (TripStatus.VALIDATED.equals(existingTrip.getStatus())) {
            throw new BusinessException("Cannot update VALIDATED trip");
        }
        existingTrip.setName(trip.getName());
        existingTrip.setDescription(trip.getDescription());
        existingTrip.setLastModified(now);
        return this.tripRepository.save(existingTrip);
    }


    @Transactional
    @Override
    public String addParticipants(String tripId, List<User> paticipants) {
        Optional<Trip> optionalTrip = this.tripRepository.findById(tripId);
        if(!optionalTrip.isPresent()) {
            return null;
        }
        Trip trip = optionalTrip.get();
        paticipants
            .stream()
            .forEach(user -> {
                User savedUser = this.userRepository.save(user);
                Participant participant = new Participant();
                participant.setTrip(trip);
                participant.setUser(savedUser);
                this.participantRepository.save(participant);
            });
        return tripId;
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
                                .coverPhoto(trip.getCoverPhoto())
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
                .coverPhoto(trip.getCoverPhoto())
                .createdDate(trip.getCreatedDate())
                .lastModified(trip.getLastModified())
                .participants(participants.stream()
                        .map(Participant::getUser)
                        .collect(Collectors.toSet()))
                .build();
    }

    @Transactional
    @Override
    public String validateTrip(String tripId) {
        Optional<Trip> optionalTrip = this.tripRepository.findById(tripId);
        if(optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            trip.setStatus(TripStatus.VALIDATED);
            return this.tripRepository.save(trip).getTripId();
        }
        return null;
    }


}
