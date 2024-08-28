package com.example.batch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.batch.entity.Employee;
import com.example.batch.entity.EmployeePhoneNumber;

public interface EmployeePhoneNumberRepository  extends JpaRepository<EmployeePhoneNumber, Integer>{
	 List<EmployeePhoneNumber> findByEmployee(Employee employee);
}
