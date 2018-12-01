package com.lightdevel.wephuot.moneymanagement.models.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "shareparts")
public class Sharepart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double amount;

    @OneToOne
    @JoinColumn(name = "spending_id")
    private Spending spending;

    @OneToOne
    @JoinColumn(name = "debiter")
    private User debiter;
}
