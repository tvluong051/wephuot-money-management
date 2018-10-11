package com.lightdevel.wephuot.moneymanagement.repositories;

import com.lightdevel.wephuot.moneymanagement.models.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
