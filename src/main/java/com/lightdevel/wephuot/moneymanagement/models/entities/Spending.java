package com.lightdevel.wephuot.moneymanagement.models.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "spendings")
public class Spending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column(name = "spent_date")
    private Long spentDate;

    @Column
    private Double amount;

    @Column(name = "equally_divided")
    private Boolean equallyDivided;

    @OneToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToOne
    @JoinColumn(name = "crediter")
    private User crediter;
}
