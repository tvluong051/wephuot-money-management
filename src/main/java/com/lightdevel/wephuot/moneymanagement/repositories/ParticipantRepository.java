package com.lightdevel.wephuot.moneymanagement.repositories;

import com.lightdevel.wephuot.moneymanagement.models.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findAllByUserUserId(String userId);

    List<Participant> findAllByTripTripId(String tripId);

    Participant findByUserUserIdAndTripTripId(String userId, String tripId);
}
