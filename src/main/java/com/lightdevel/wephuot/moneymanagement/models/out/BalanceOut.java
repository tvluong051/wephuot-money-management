package com.lightdevel.wephuot.moneymanagement.models.out;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class BalanceOut {
  private Map<String, Double> credits;

  private Map<String, Double> debits;
}
