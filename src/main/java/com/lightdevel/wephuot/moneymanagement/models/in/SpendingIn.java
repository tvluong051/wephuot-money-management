package com.lightdevel.wephuot.moneymanagement.models.in;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class SpendingIn {
    @NotNull
    private String description;

    @NotNull
    private Long spentDate;

    @NotNull
    private Double amount;

    @NotNull
    private String crediter;

    private Boolean equallyDivided;

    private Map<String, Double> shareparts;
}
