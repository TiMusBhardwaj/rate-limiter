package com.company.ratelimit;

public interface IRateLimit {
	
	public default boolean rateLimit(String userId, String api) {
		return false;
	}

}
