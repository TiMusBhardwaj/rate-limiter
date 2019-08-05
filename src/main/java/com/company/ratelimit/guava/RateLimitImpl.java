package com.company.ratelimit.guava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.ratelimit.IRateLimit;

/**
 * This IRateLimit implementation uses guava local cache.
 * This cache could be replaced by any other caching tool.
 * 
 * We can also use a distributed cache.
 * 
 * @author sumit.bhardwaj
 *
 */
@Component
public class RateLimitImpl implements IRateLimit{
	
	@Autowired
	private GuavaConfig config;
	
	public boolean rateLimit(String userId, String api) {
		return config.rateLimit(userId, api);
	}

}
