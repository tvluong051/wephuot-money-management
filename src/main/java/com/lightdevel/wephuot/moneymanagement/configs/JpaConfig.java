package com.lightdevel.wephuot.moneymanagement.configs;

import com.lightdevel.wephuot.moneymanagement.models.entities.Trip;
import com.lightdevel.wephuot.moneymanagement.repositories.TripRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = { TripRepository.class })
@EntityScan(basePackageClasses = { Trip.class })
public class JpaConfig {
}
