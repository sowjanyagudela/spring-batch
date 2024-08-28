package com.example.batch.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.batch.dto.EmployeeRequest;
import com.example.batch.dto.EmployeeResponse;
import com.example.batch.entity.Employee;
import com.example.batch.entity.EmployeePhoneNumber;
import com.example.batch.exception.ProcessingException;
import com.example.batch.repository.EmployeeRepository;
import com.example.batch.util.SystemType;

@Service
public class EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CommonService commonService;

	public EmployeeResponse getEmployeeByIdOrEmail(Integer id, String email) {
		EmployeeResponse employeeResponse = new EmployeeResponse();
		Employee employee = null;
		if (Objects.isNull(id) && Objects.isNull(email)) {
			throw new ProcessingException("Id or Email is mandatory", 1000, SystemType.MS, HttpStatus.BAD_REQUEST);
		}

		if (Objects.nonNull(id)) {
			employee = employeeRepository.findById(id)
					.orElseThrow(() -> new ProcessingException("Employee not found with Id: " + id, 1002, SystemType.MS,
							HttpStatus.NOT_FOUND));
		}

		if (StringUtils.isNotEmpty(email)) {
			employee = employeeRepository.findByEmail(email)
					.orElseThrow(() -> new ProcessingException("Employee not found with Email: " + email, 1003,
							SystemType.MS, HttpStatus.NOT_FOUND));
		}

		employeeResponse.setStatus(HttpStatus.CREATED.value());
		employeeResponse.setTime(commonService.getDate(new Date()));
		employeeResponse.setCorrelationId(UUID.randomUUID().toString());
		employeeResponse.setData(employee);
		return employeeResponse;
	}

	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	public EmployeeResponse addEmployee(EmployeeRequest employeeRequest) {
		EmployeeResponse employeeResponse = new EmployeeResponse();
		Employee employee = createAndSaveEmployee(employeeRequest);

		employeeResponse.setStatus(HttpStatus.CREATED.value());
		employeeResponse.setTime(commonService.getDate(new Date()));
		employeeResponse.setCorrelationId(UUID.randomUUID().toString());
		employeeResponse.setData(employee);

		logger.info(" Employee Saved with Id {}", employee.getEmployeeId());
		return employeeResponse;
	}

	private Employee createAndSaveEmployee(EmployeeRequest employeeRequest) {
		Employee employee = new Employee();
		employee.setFirstName(employeeRequest.getFirstName());
		employee.setLastName(employeeRequest.getLastName());
		employee.setEmail(employeeRequest.getEmail());
		employee.setDoj(employeeRequest.getDoj());
		employee.setSalary(BigDecimal.valueOf(employeeRequest.getSalary()));
		List<EmployeePhoneNumber> phoneNumbers = updateEmpPhoneNumber(employeeRequest, employee);

		employee.setPhoneNumbers(phoneNumbers);
		employeeRepository.save(employee);
		return employee;
	}

	private List<EmployeePhoneNumber> updateEmpPhoneNumber(EmployeeRequest employeeRequest, Employee employee) {
		List<EmployeePhoneNumber> newPhoneNumbers = employeeRequest.getPhoneNumber().stream().map(phone -> {
			EmployeePhoneNumber phoneNumber = new EmployeePhoneNumber();
			phoneNumber.setPhoneNumber(phone);
			phoneNumber.setEmployee(employee);
			return phoneNumber;
		}).collect(Collectors.toList());
		return newPhoneNumbers;
	}

}
