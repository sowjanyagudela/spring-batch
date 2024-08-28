package com.example.batch.exception;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.batch.dto.MSApplicationErrorRepo;
import com.example.batch.service.CommonService;
import com.example.batch.util.ErrorCodes;
import com.example.batch.util.ErrorDetails;


@ControllerAdvice
public class CustomizedMSExceptionHandler  extends ResponseEntityExceptionHandler{
	
	@Autowired
	MSApplicationErrorRepo msApplicationErrorRepo;
	
	@Autowired
	CommonService commonService;
	
	@ExceptionHandler(ProcessingException.class)
	public final ResponseEntity<ErrorDetails> handleProcessingException(ProcessingException ex){
		String errorMessage = ex.getMessage();
		if(StringUtils.isBlank(errorMessage)) {
			errorMessage = "Failed due to bad request";
		}
		int errorCode= ex.getCode();
		HttpStatus statusCode;
		Enum<ErrorCodes> errorCodes = msApplicationErrorRepo.getErrorMap().get(ex.getCode());
		if(Objects.nonNull(errorCodes)) {
			statusCode = ErrorCodes.valueOf(errorCodes.name()).getStatus();
		}else {
			statusCode=HttpStatus.UNPROCESSABLE_ENTITY;
			errorCode=422;
		}
		
		ErrorDetails errorDetails = createRawMsError();
		errorDetails.setMessage(errorMessage);
		errorDetails.setCode(errorCode);
		errorDetails.setStatus(statusCode);
		return ResponseEntity.status(statusCode).body(errorDetails);
		
	}
	
	private ErrorDetails createRawMsError() {
		 ErrorDetails errorDetails = new  ErrorDetails();
		 errorDetails.setTime(commonService.getDate(new Date()));
		 errorDetails.setCorrelationId(UUID.randomUUID().toString());
		 return errorDetails;
	}
}
