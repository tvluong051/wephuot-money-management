package com.lightdevel.wephuot.moneymanagement.services;

import com.lightdevel.wephuot.moneymanagement.models.entities.Sharepart;
import com.lightdevel.wephuot.moneymanagement.models.entities.Spending;
import com.lightdevel.wephuot.moneymanagement.models.out.SpendingOut;
import com.lightdevel.wephuot.moneymanagement.repositories.SharepartRepository;
import com.lightdevel.wephuot.moneymanagement.repositories.SpendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SpendingServiceImpl implements SpendingService {

    private SpendingRepository spendingRepository;
    private SharepartRepository sharepartRepository;

    @Autowired
    public SpendingServiceImpl(SpendingRepository spendingRepository, SharepartRepository sharepartRepository) {
        this.spendingRepository = Objects.requireNonNull(spendingRepository);
        this.sharepartRepository = Objects.requireNonNull(sharepartRepository);
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
}
