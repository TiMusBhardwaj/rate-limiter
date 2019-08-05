package com.company.ratelimit;

import java.io.Serializable;

import lombok.Getter;

@Getter
public final class RateLimitClientKey implements Serializable{
	
	private String userId;
	private String api;
	
	private RateLimitClientKey(String userId, String api) {
		this.userId = userId;
		this.api = api;
	}
	
	/**
	 * 
	 * TODO: Use flyweight design pattern.
	 * This will save us a lot of memory.
	 * 
	 * @param userId
	 * @param api
	 * @return 
	 */
	public static RateLimitClientKey getInstance(String userId, String api) {
		return new RateLimitClientKey(userId, api);
	}
	
	
	public boolean equals(Object o) {
		 
		// null check
		if (o == null) {
			return false;
		}
 
		// this instance check
		if (this == o) {
			return true;
		}
 
		// instanceof Check and actual value check
		if (o instanceof RateLimitClientKey) {
			if (!equals(this.userId, ((RateLimitClientKey) o).getUserId())) {
				return false;
			}
			
			if (!equals(this.api, ((RateLimitClientKey) o).getApi())) {
				return false;
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode() 
    { 
		return userId.hashCode();
    }
	
	private static boolean equals(String s1, String s2){
	    if(s1 == null)
	        return s2 == null;
	    return s1.equals(s2);
	}

}
