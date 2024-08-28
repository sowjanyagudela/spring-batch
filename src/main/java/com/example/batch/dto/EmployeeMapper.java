package com.example.batch.dto;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class EmployeeMapper  implements FieldSetMapper<EmployeeRequest>{

	@Override
	public EmployeeRequest mapFieldSet(FieldSet fieldSet) throws BindException {
		EmployeeRequest employeeRequest = new EmployeeRequest();
		  String employeeIdString = fieldSet.readString("employeeId").trim();
          if (!employeeIdString.isEmpty()) {
              employeeRequest.setEmployeeId(Integer.valueOf(employeeIdString));
          } else {
              throw new BindException(employeeRequest,"Employee ID is missing or invalid");
          }
		employeeRequest.setFirstName(fieldSet.readString("firstName"));
		employeeRequest.setLastName(fieldSet.readString("lastName"));
		employeeRequest.setEmail(fieldSet.readString("email"));
		employeeRequest.setDoj(fieldSet.readDate("doj", "yyyy-MM-dd"));
		employeeRequest.setSalary(fieldSet.readDouble("salary"));

		String phoneNumbersString = fieldSet.readString("phoneNumber");
		List<String> phoneNumbers = Arrays.asList(phoneNumbersString.split(","));
		employeeRequest.setPhoneNumber(phoneNumbers);
		return employeeRequest;
	}

}
