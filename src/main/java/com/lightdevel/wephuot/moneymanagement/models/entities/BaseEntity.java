package com.lightdevel.wephuot.moneymanagement.models.entities;

import lombok.Data;
import org.joda.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity {
    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "last_modified")
    private Long lastModified;
}
