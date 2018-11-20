package com.lightdevel.wephuot.moneymanagement.models.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lightdevel.wephuot.moneymanagement.models.entities.User;
import com.lightdevel.wephuot.moneymanagement.models.enums.TripStatus;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripOut {
    private String tripId;
    private String name;
    private String description;
    private TripStatus status;
    private Long createdDate;
    private Long lastModified;
    private String coverPhoto;
    private Set<User> participants;
}
