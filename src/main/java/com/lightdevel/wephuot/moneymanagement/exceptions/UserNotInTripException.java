package com.lightdevel.wephuot.moneymanagement.exceptions;

public class UserNotInTripException extends BusinessException {
	public UserNotInTripException(String tripId, String userId) {
		super("User " + userId + " doesn't exist or doesn't participate to trip " + tripId);
	}
}
