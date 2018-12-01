package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.models.out.SpendingOut;

import java.util.List;

public interface SpendingService {
    List<SpendingOut> getAllSpendingsOfTrip(String tripId);
}
