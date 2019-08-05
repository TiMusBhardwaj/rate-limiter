package com.company.ratelimit;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Sliding logs algorithm is used to rate Limit.
 * To Save space request received in 1 sec span are accumulated together.
 * request count for each second saved in reqPerTimeStamp. 
 * This bucket could be further decreased.
 * 
 * 
 * Counter is maintained just to avoid calculation of requests in rateLimitWindow
 * each time we receive a request.
 * 
 * @author sumit.bhardwaj
 *
 */
public class RateLimitClientValue implements Serializable {

	/**
	 * Request count history for each timestamp.
	 * just to save space
	 */
	private Map<Instant, Integer> reqPerTimeStamp;
	private Integer threshold;
	private AtomicInteger counter;
	private Object mutex = new Object();
	
	/**
	 * Rate Limit window in seconds
	 */
	private Long rateLimitWindow;

	private RateLimitClientValue(Integer threshold, Long rateLimitWindow) {
		this.reqPerTimeStamp = new ConcurrentHashMap<Instant, Integer>();
		this.threshold = threshold;
		this.counter = new AtomicInteger(0);
		this.rateLimitWindow = rateLimitWindow;
	}

	public static RateLimitClientValue getInsClientValue(Integer threshold, Long rateLimitWindow) {
		return new RateLimitClientValue(threshold, rateLimitWindow);

	}

	/**
	 * Check if counter reached threshold
	 * Initiate rebalance, if counter is greater than threshold.
	 * Then check again.
	 * @param instant
	 * @return True for blocking
	 */
	public boolean permit(Instant instant) {
		if (!checkAndInc(instant)) {
			return false;
		} else {
			rebalance();
			return checkAndInc(instant);
		}
	}

	/**
	 * @param instant
	 * @return True for blocking request
	 *  False for non-blocking request and inc the counter.
	 */
	private boolean checkAndInc(Instant instant) {
		if (threshold > counter.get()) {
			counter.incrementAndGet();
			reqPerTimeStamp.merge(instant.truncatedTo(ChronoUnit.SECONDS), 1, (x, y) -> x + y);
			return false;
		}
		return true;
	}

	/**
	 * Remove Entries older than Rate limit window
	 */
	public void rebalance() {
		synchronized (mutex) {
			if (threshold <= counter.get()) {
				Instant instant = Instant.now().minus(rateLimitWindow, ChronoUnit.SECONDS)
						.truncatedTo(ChronoUnit.SECONDS);
				if (reqPerTimeStamp.keySet().removeIf(x -> x.isBefore(instant))) {
					int counter = reqPerTimeStamp.values().stream().mapToInt(x -> x).sum();
					this.counter = new AtomicInteger(counter);
				}

			}

		}

	}

}
