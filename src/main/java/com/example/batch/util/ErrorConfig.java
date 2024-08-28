package com.example.batch.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.batch.dto.MSApplicationErrorRepo;

@Configuration
public class ErrorConfig {

	@Bean
	public MSApplicationErrorRepo getMsApplicationErrorRepo() {
		MSApplicationErrorRepo msApplicationErrorRepo = new MSApplicationErrorRepo();
		for(ErrorCodes errorCode: ErrorCodes.values()) {
			msApplicationErrorRepo.getErrorMap().put(errorCode.getCode(), errorCode);
		}
		return msApplicationErrorRepo;
	}
}
