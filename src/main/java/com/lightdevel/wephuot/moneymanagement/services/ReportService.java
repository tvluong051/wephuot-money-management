package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.models.out.BalanceOut;

public interface ReportService {

  BalanceOut getBalance(String tripId, String userId);

}
