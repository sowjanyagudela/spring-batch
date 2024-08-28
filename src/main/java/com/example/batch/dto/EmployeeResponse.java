package com.example.batch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponse {


	private Integer status;
	private String time;
	private Object data;
	private String correlationId;
}
