package com.company.ratelimit.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="USER_API_LIMIT")
public class ApiLimitEnity {
	
	@Id
	private String id;

	private String userId;
	
	private String api;
	
	private Integer rateLimit;
}
