package com.example.batch.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employee", schema = "img_batch")
@Getter
@Setter
public class Employee {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer employeeId;

	private String firstName;

	private String lastName;

	private String email;

	private Date doj;

	private BigDecimal salary;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<EmployeePhoneNumber> phoneNumbers;
}
