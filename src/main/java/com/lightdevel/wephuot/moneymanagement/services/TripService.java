package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.models.entities.User;
import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;
import com.lightdevel.wephuot.moneymanagement.models.out.TripOut;

import java.util.List;
import java.util.Set;

public interface TripService {
    String save(TripIn trip);

    String addParticipants(String tripId, List<User> paticipants);

    Set<TripOut> getAllTripsOfUser(String userId);

    TripOut getDetail(String tripId);

    String validateTrip(String tripId);
}
