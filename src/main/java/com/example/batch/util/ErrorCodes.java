package com.example.batch.util;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCodes {

	MISSING_BODY_INFORMATION(800,HttpStatus.BAD_REQUEST,"Missing body information",SystemType.MS),

	COMMON_ERROR(900,HttpStatus.BAD_REQUEST,"An exception occurred due to service unavailability",SystemType.MS),
	;

	
	private int code;
	private HttpStatus status;
	private SystemType systemType;
	private String errorMessage;

	private ErrorCodes(int code, HttpStatus status, String errorMessage, SystemType systemType) {
		this.code = code;
		this.status = status;
		this.systemType = systemType;
		this.errorMessage = errorMessage;
	}
}