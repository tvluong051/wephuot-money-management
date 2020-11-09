package com.lightdevel.wephuot.moneymanagement.models.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lightdevel.wephuot.moneymanagement.models.entities.User;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpendingOut {

    private Long id;

    private String description;

    private Long spentDate;

    private Double amount;

    private User crediter;

    private boolean equallyDivided;

    private Map<String, Double> shareparts;
}
