package com.company.ratelimit.guava;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.ratelimit.RateLimitClientKey;
import com.company.ratelimit.RateLimitClientValue;
import com.company.ratelimit.entity.ApiLimitEnity;
import com.company.ratelimit.repo.ApiLimitRepo;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GuavaConfig {
	
	
	@Autowired
	private ApiLimitRepo apiLimitRepo;
	
	/**
	 * Time window to check request count vs threshold (seconds).
	 *  
	 */
	@Value("${rateLimitWindow:60}")
	private Long rateLimitWindow;
	
	
	/**
	 * Default threshold, if userid or api is not registered 
	 * for rate limit 
	 */
	@Value("${defaultThreshold:100}")
	private Integer defaultThreshold; 
	
	private LoadingCache<RateLimitClientKey, RateLimitClientValue> rateLimit = CacheBuilder.newBuilder()
			.build(new CacheLoader<RateLimitClientKey, RateLimitClientValue>() {

				@Override
				public RateLimitClientValue load(RateLimitClientKey key) throws Exception {
					Integer threshold = staticRateLimit.get(key);
					
					return RateLimitClientValue.getInsClientValue(threshold, rateLimitWindow);
				}

			});
	
	
	private LoadingCache<RateLimitClientKey, Integer> staticRateLimit = CacheBuilder.newBuilder()
			.expireAfterWrite(2, TimeUnit.HOURS)
			.build(new CacheLoader<RateLimitClientKey, Integer>() {

				@Override
				public Integer load(RateLimitClientKey key) throws Exception {

					List<ApiLimitEnity> lst= apiLimitRepo.findByUserIdAndApi(key.getUserId(), key.getApi());
					if (lst.size() ==0) {
						return defaultThreshold;
					}
					return lst.get(0).getRateLimit();
				}

			});
	
	
	public boolean rateLimit(String userId, String api) {
		try {
			RateLimitClientValue val = rateLimit.get(RateLimitClientKey.getInstance(userId, api));
			return val.permit(Instant.now());
		} catch (ExecutionException e) {
			log.error("Error while checking limit for userId: {}, api: {}", userId, api, e);	
			
		}
		return false;
	}

}
