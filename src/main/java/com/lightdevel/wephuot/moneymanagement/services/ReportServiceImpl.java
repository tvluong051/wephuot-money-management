package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.exceptions.UserNotInTripException;
import com.lightdevel.wephuot.moneymanagement.models.entities.Sharepart;
import com.lightdevel.wephuot.moneymanagement.models.entities.Spending;
import com.lightdevel.wephuot.moneymanagement.models.out.BalanceOut;
import com.lightdevel.wephuot.moneymanagement.repositories.ParticipantRepository;
import com.lightdevel.wephuot.moneymanagement.repositories.SharepartRepository;
import com.lightdevel.wephuot.moneymanagement.repositories.SpendingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

  private ParticipantRepository participantRepository;
  private SpendingRepository spendingRepository;
  private SharepartRepository sharepartRepository;

  public ReportServiceImpl(ParticipantRepository participantRepository,
                           SpendingRepository spendingRepository,
                           SharepartRepository sharepartRepository) {
    this.participantRepository = Objects.requireNonNull(participantRepository);
    this.spendingRepository = Objects.requireNonNull(spendingRepository);
    this.sharepartRepository = Objects.requireNonNull(sharepartRepository);
  }

  @Override
  public BalanceOut getBalance(String tripId, String userId) {
    if(this.participantRepository.findByUserUserIdAndTripTripId(userId, tripId) == null) {
      LOGGER.error("User = {} not in trip = {}", userId, tripId);
      throw new UserNotInTripException(tripId, userId);
    }
    Set<String> participantsIds = this.participantRepository.findAllByTripTripId(tripId)
      .stream()
      .map(participant -> participant.getUser().getUserId())
      .collect(Collectors.toSet());

    List<Spending> spendings = this.spendingRepository.findAllByTripTripId(tripId);
    Map<String, Double> credits = new HashMap<>();
    Map<String, Double> debits = new HashMap<>();

    for (Spending spending: spendings) {
      String crediterId = spending.getCrediter().getUserId();
      if (!credits.containsKey(crediterId)) {
        credits.put(crediterId, spending.getAmount());
      } else {
        credits.put(crediterId, credits.get(crediterId) + spending.getAmount());
      }

      if (spending.getEquallyDivided()) {
        for (String participantId: participantsIds) {
          Double amount = spending.getAmount() / participantsIds.size();
          if (!debits.containsKey(participantId)) {
            debits.put(participantId, amount);
          } else {
            debits.put(participantId, debits.get(participantId) + amount);
          }
        }
      } else {
        List<Sharepart> shareparts = this.sharepartRepository.findAllBySpendingId(spending.getId());
        for (Sharepart sharepart: shareparts) {
          String debiterId = sharepart.getDebiter().getUserId();
          if (!debits.containsKey(debiterId)) {
            debits.put(debiterId, sharepart.getAmount());
          } else {
            debits.put(debiterId, debits.get(debiterId) + sharepart.getAmount());
          }
        }
      }

    }

    return new BalanceOut(credits, debits);
  }
}
