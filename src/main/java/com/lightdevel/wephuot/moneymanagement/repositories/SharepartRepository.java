package com.lightdevel.wephuot.moneymanagement.repositories;

import com.lightdevel.wephuot.moneymanagement.models.entities.Sharepart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface SharepartRepository extends JpaRepository<Sharepart, Long> {
    List<Sharepart> findAllBySpendingId(Long spendingId);

    @Modifying
    int deleteBySpendingId(Long spendingId);
}
