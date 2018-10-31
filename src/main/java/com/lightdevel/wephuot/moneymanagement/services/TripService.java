package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.models.entities.Trip;
import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;

import java.util.Set;

public interface TripService {
    String save(TripIn trip);

    Set<Trip> getAllTripsOfUser(String userId);
}
