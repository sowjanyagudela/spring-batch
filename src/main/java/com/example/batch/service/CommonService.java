package com.example.batch.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.batch.exception.ProcessingException;
import com.example.batch.util.ErrorCodes;
import com.example.batch.util.SystemType;

@Service
public class CommonService {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonService.class);
	
	public void throwProcessingException(Exception e , String message) {
		logger.debug("Error Message:: {}",message);
		
		if(e instanceof ProcessingException) {
			ProcessingException exception =(ProcessingException) e;
			throw new ProcessingException(exception.getMessage(), exception.getCode(), exception.getSystemType(), exception.getHttpStatus());
			
		}else {
			ErrorCodes commonError = ErrorCodes.COMMON_ERROR;
			String error="An exception occured:"+e.getMessage();
			throw new ProcessingException(error,commonError.getCode(),SystemType.MS,HttpStatus.BAD_REQUEST);
		}
	}
	
	public String getDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return simpleDateFormat.format(date);
	}
	
	public String getDateOfJoining(String doj) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(doj);
	}
}