package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.models.in.TripIn;
import com.lightdevel.wephuot.moneymanagement.models.out.TripOut;

import java.util.Set;

public interface TripService {
    String save(TripIn trip);

    Set<TripOut> getAllTripsOfUser(String userId);

    TripOut getDetail(String tripId);
}
