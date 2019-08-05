package com.company.ratelimit.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.company.ratelimit.IRateLimit;
import com.company.ratelimit.filter.RateLimitFilter;

@EnableCaching
@Configuration
@EntityScan(basePackages= {"com.company.ratelimit.entity"})
@EnableJpaRepositories(basePackages= {"com.company.ratelimit.repo"})
public class RateLimitConfig {

	/**
	 * @param rateLimitFilter
	 * @return 
	 */
	@Bean
	public FilterRegistrationBean<RateLimitFilter> filterRegistrationBean(RateLimitFilter rateLimitFilter) {
		FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();


		registrationBean.setFilter(rateLimitFilter);
		//We can Filter out Url which are not part of rate limit
		registrationBean.addUrlPatterns("/api/*");
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}
	
	/**
	 * @param rateLimiter
	 * @return
	 */
	@Bean
	public RateLimitFilter rateLimitFilter(IRateLimit rateLimiter) {
		return new RateLimitFilter(rateLimiter);
	}

}
