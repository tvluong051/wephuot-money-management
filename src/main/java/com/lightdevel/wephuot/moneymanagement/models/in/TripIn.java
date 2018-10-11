package com.lightdevel.wephuot.moneymanagement.models.in;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class TripIn {
    private String tripId;
    @NotNull
    private String name;
    private String description;
}
