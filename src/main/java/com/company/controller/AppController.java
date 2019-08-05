package com.company.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

	private static final String SUCCESS = "Success";
	
	@GetMapping("/api/v1/developers")
	public String getDeveloper() {
		
		return SUCCESS;
	}
	
	@GetMapping("/api/v1/organisation")
	public String getOrganisation() {
		
		return SUCCESS;
	}
}
