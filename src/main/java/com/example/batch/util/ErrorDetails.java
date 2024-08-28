package com.example.batch.util;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetails {

	private Integer code;
	private HttpStatus status;
	private String time;
	private String message;
	private String correlationId;
}