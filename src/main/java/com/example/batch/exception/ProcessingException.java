package com.example.batch.exception;

import org.springframework.http.HttpStatus;

import com.example.batch.util.SystemType;

import lombok.Getter;

@Getter
public class ProcessingException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String message;
	private int code;
	private SystemType systemType;
	private HttpStatus httpStatus;
	
	public ProcessingException(String message, int code, SystemType systemType, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.code = code;
		this.systemType = systemType;
		this.httpStatus = httpStatus;
	}
	
	
}