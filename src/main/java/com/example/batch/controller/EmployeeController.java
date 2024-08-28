package com.example.batch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.batch.dto.EmployeeRequest;
import com.example.batch.dto.EmployeeResponse;
import com.example.batch.entity.Employee;
import com.example.batch.service.CommonService;
import com.example.batch.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CommonService commonService;

	@GetMapping
	public EmployeeResponse getEmployee(@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String email) {
		EmployeeResponse employeeResponse = null;
		try {
			employeeResponse = employeeService.getEmployeeByIdOrEmail(id, email);

		} catch (Exception e) {
			commonService.throwProcessingException(e, "Exception occured while retrieving the employee data");
		} finally {
		}

		return employeeResponse;

	}

	@PostMapping("/add")
	@ResponseStatus(HttpStatus.CREATED)
	public EmployeeResponse saveEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
		EmployeeResponse employeeResponse = null;
		Long startTime = System.currentTimeMillis();
		logger.info("Received a POST request to create  a employee");
		try {
			employeeResponse = employeeService.addEmployee(employeeRequest);

		} catch (Exception e) {
			commonService.throwProcessingException(e, "Exception occured while saving the employee data");
		} finally {
			Long endTime = System.currentTimeMillis();
			logger.info("Time taken to processed  a POST request completed to  create  a employee {}",
					(endTime - startTime) / 1000 + "Seconds");
		}
		return employeeResponse;
	}
}
