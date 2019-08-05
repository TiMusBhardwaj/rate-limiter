package com.company.ratelimit.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.company.ratelimit.entity.ApiLimitEnity;

public interface ApiLimitRepo extends CrudRepository<ApiLimitEnity, String>{
	
	public List<ApiLimitEnity> findByUserIdAndApi(String userId, String api);

}
