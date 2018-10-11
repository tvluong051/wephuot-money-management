package com.lightdevel.wephuot.moneymanagement.models.entities;

import com.lightdevel.wephuot.moneymanagement.models.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @Column(name = "trip_id")
    private String tripId;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    @Enumerated
    private TripStatus status;
}
