package com.lightdevel.wephuot.moneymanagement.repositories;

import com.lightdevel.wephuot.moneymanagement.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
