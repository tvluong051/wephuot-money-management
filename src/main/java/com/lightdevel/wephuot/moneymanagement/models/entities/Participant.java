package com.lightdevel.wephuot.moneymanagement.models.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
