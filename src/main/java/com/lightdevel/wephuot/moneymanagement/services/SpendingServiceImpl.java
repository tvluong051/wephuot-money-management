package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.exceptions.BusinessException;
import com.lightdevel.wephuot.moneymanagement.exceptions.UserNotInTripException;
import com.lightdevel.wephuot.moneymanagement.models.entities.*;
import com.lightdevel.wephuot.moneymanagement.models.in.SpendingIn;
import com.lightdevel.wephuot.moneymanagement.models.out.SpendingOut;
import com.lightdevel.wephuot.moneymanagement.repositories.ParticipantRepository;
import com.lightdevel.wephuot.moneymanagement.repositories.SharepartRepository;
import com.lightdevel.wephuot.moneymanagement.repositories.SpendingRepository;
import com.lightdevel.wephuot.moneymanagement.repositories.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpendingServiceImpl implements SpendingService {
    private static Logger LOGGER = LoggerFactory.getLogger(SpendingServiceImpl.class);

    private SpendingRepository spendingRepository;
    private SharepartRepository sharepartRepository;
    private TripRepository tripRepository;
    private ParticipantRepository participantRepository;

    @Autowired
    public SpendingServiceImpl(SpendingRepository spendingRepository,
                               SharepartRepository sharepartRepository,
                               TripRepository tripRepository,
                               ParticipantRepository participantRepository) {
        this.spendingRepository = Objects.requireNonNull(spendingRepository);
        this.sharepartRepository = Objects.requireNonNull(sharepartRepository);
        this.tripRepository = Objects.requireNonNull(tripRepository);
        this.participantRepository = Objects.requireNonNull(participantRepository);
    }

    @Override
    public List<SpendingOut> getAllSpendingsOfTrip(String tripId) {
        List<Spending> spendings = this.spendingRepository.findAllByTripTripId(tripId);
        if(spendings.size() == 0) return new ArrayList<>();
        return spendings.stream()
                .map(spending -> {
                    List<Sharepart> shareparts = this.sharepartRepository.findAllBySpendingId(spending.getId());
                    SpendingOut.SpendingOutBuilder builder = SpendingOut.builder()
                            .id(spending.getId())
                            .description(spending.getDescription())
                            .spentDate(spending.getSpentDate())
                            .amount(spending.getAmount())
                            .equallyDivided(spending.getEquallyDivided() == null ? false : spending.getEquallyDivided())
                            .crediter(spending.getCrediter());
                    if(shareparts.size() == 0) {
                        builder.shareparts(new HashMap<>());
                    } else {
                        builder.shareparts(
                            shareparts.stream()
                                .collect(Collectors.toMap(
                                        sharepart -> sharepart.getDebiter().getUserId(),
                                        Sharepart::getAmount)
                                )
                        );
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSpending(Long spendingId) {
        int rowCount = this.sharepartRepository.deleteBySpendingId(spendingId);
        LOGGER.info("Delete {} row(s) from Sharepart table", rowCount);
        if (this.spendingRepository.existsById(spendingId)) {
            this.spendingRepository.deleteById(spendingId);
            LOGGER.info("Delete spending id = {}", spendingId);
        } else {
            LOGGER.warn("Spending id = {} doesn't actual exist", spendingId);
        }
    }

    @Override
    @Transactional
    public SpendingOut addSpending(String tripId, SpendingIn spendingIn) {
        Optional<Trip> optionalTrip = this.tripRepository.findById(tripId);
        if(!optionalTrip.isPresent()) throw new BusinessException("Trip doesn't exist");
        Participant participant = this.participantRepository.findByUserUserIdAndTripTripId(spendingIn.getCrediter(), tripId);
        if(participant == null) {
            LOGGER.error("Crediter = {} not in trip = {}", spendingIn.getCrediter(), tripId);
            throw new UserNotInTripException(tripId, spendingIn.getCrediter());
        }

        Trip trip = optionalTrip.get();
        User crediter = participant.getUser();

        Spending spending = new Spending();
        spending.setTrip(trip);
        return saveSpending(spending, spendingIn, crediter, false);
    }

    @Override
    @Transactional
    public SpendingOut updateSpending(Long spendingId, SpendingIn spendingIn) {
        Optional<Spending> optionalSpending = this.spendingRepository.findById(spendingId);
        if(!optionalSpending.isPresent()) {
            LOGGER.error("Spending id = {} doesn't exist", spendingId);
            throw new BusinessException("Spending id = " + spendingId + " doesn't exist");
        }
        Spending spending = optionalSpending.get();
        Participant participant = this.participantRepository.findByUserUserIdAndTripTripId(spendingIn.getCrediter(), spending.getTrip().getTripId());
        if(participant == null) {
            LOGGER.error("Crediter = {} not in trip = {}", spendingIn.getCrediter(), spending.getTrip().getTripId());
            throw new UserNotInTripException(spending.getTrip().getTripId(), spendingIn.getCrediter());
        }
        User crediter = participant.getUser();
        return saveSpending(spending, spendingIn, crediter, true);
    }

    SpendingOut saveSpending(Spending newSpending, SpendingIn newSpendingIn, User crediter, boolean isUpdating) {
        newSpending.setDescription(newSpendingIn.getDescription());
        newSpending.setSpentDate(newSpendingIn.getSpentDate());
        newSpending.setAmount(newSpendingIn.getAmount());
        newSpending.setCrediter(crediter);

        boolean equallyDivided = isEquallyDivided(newSpendingIn);

        newSpending.setEquallyDivided(equallyDivided);
        newSpending = this.spendingRepository.save(newSpending);
        SpendingOut.SpendingOutBuilder builder = SpendingOut.builder()
                .id(newSpending.getId())
                .description(newSpending.getDescription())
                .spentDate(newSpending.getSpentDate())
                .amount(newSpending.getAmount())
                .equallyDivided(newSpending.getEquallyDivided() == null ? false : newSpending.getEquallyDivided())
                .crediter(newSpending.getCrediter());

        if(!equallyDivided) {
            List<Sharepart> shareparts = saveShareparts(newSpending, newSpendingIn.getShareparts());
            builder.shareparts(shareparts.stream()
                    .collect(Collectors.toMap(s -> s.getDebiter().getUserId(), Sharepart::getAmount))
            );
        } else if(isUpdating) {
            Long spendingId = newSpending.getId();
            int rowCount = this.sharepartRepository.deleteBySpendingId(spendingId);
            LOGGER.info("Delete {} row(s) from Sharepart table", rowCount);
        }
        return builder.build();
    }

    boolean isEquallyDivided(SpendingIn spendingIn) {
        if(spendingIn.getEquallyDivided() != null) {
            return spendingIn.getEquallyDivided();
        }
        if (CollectionUtils.isEmpty(spendingIn.getShareparts())) {
            return true;
        }
        return false;

    }

    List<Sharepart> saveShareparts(Spending spending, Map<String, Double> shareparts) {
        List<Sharepart> existingShareparts = this.sharepartRepository.findAllBySpendingId(spending.getId());
        List<Sharepart> result = new ArrayList<>();
        for(Sharepart existingSharepart : existingShareparts) {
            User debiter = existingSharepart.getDebiter();
            existingSharepart.setAmount(shareparts.get(debiter.getUserId()));
            result.add(this.sharepartRepository.save(existingSharepart));
            shareparts.remove(debiter.getUserId());
        }
        for(String debiterId: shareparts.keySet()) {
            Participant participant = this.participantRepository.findByUserUserIdAndTripTripId(debiterId, spending.getTrip().getTripId());
            if(participant == null) {
                LOGGER.error("Debiter = {} not in trip = {}", debiterId, spending.getTrip().getTripId());
                throw new UserNotInTripException(spending.getTrip().getTripId(), debiterId);
            }
            User debiter = participant.getUser();
            Sharepart sharepart = new Sharepart();
            sharepart.setSpending(spending);
            sharepart.setAmount(shareparts.get(debiterId));
            sharepart.setDebiter(debiter);
            result.add(this.sharepartRepository.save(sharepart));
        }
        return result;
    }

}
