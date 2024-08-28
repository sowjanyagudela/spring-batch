package com.example.batch.dto;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequest {
	
	private Integer employeeId;
	
	@NotBlank(message = "First name should not be empty")
	private String firstName;
	
	@NotBlank(message = "Last name should not be empty")
	private String lastName;
	
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is mandatory")
	private String email;
	
	private List<String>phoneNumber;
	
	@NotNull(message = "Salary is mandatory")
	@Min(value = 1, message = "Salary must be greater than 0")
	private Double salary;
	
	@NotNull(message = "Date of joining is mandatory")
	private Date doj;

}