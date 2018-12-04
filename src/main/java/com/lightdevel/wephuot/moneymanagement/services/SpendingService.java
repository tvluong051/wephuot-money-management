package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.models.in.SpendingIn;
import com.lightdevel.wephuot.moneymanagement.models.out.SpendingOut;

import java.util.List;

public interface SpendingService {
    List<SpendingOut> getAllSpendingsOfTrip(String tripId);

    void deleteSpending(Long spendingId);

    SpendingOut saveSpending(String tripId, SpendingIn spendingIn);

    SpendingOut updateSpending(Long spendingId, SpendingIn spendingIn);
}
