package com.company.ratelimit.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.company.ratelimit.IRateLimit;

import lombok.AllArgsConstructor;
/**
 * Filter to block api request for which threshold is reached.
 * 
 * @author sumit.bhardwaj
 *
 */
@AllArgsConstructor
public class RateLimitFilter implements Filter {
	
	
	private IRateLimit rateLimiter;
	
	

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        request.getHeaderNames();
        
        String userId = request.getHeader("userId");
        String api = request.getRequestURI();
        
        
        if (StringUtils.isEmpty(userId) ||  rateLimiter.rateLimit(userId, api)) {
            
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {            
                fc.doFilter(request, response);            
        }

    }
}