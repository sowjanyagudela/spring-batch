package com.example.batch.job1.config;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batch.dto.EmployeeMapper;
import com.example.batch.dto.EmployeeRequest;
import com.example.batch.entity.BatchErrors;
import com.example.batch.entity.Employee;
import com.example.batch.entity.EmployeePhoneNumber;
import com.example.batch.job1.listener.JobCompletionNotificationListener;
import com.example.batch.repository.BatchErrorsRepository;
import com.example.batch.repository.EmployeePhoneNumberRepository;
import com.example.batch.repository.EmployeeRepository;

@Configuration
public class EmployeeProcessorJobConfig {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeProcessorJobConfig.class);

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeePhoneNumberRepository employeePhoneNumberRepository;
	
	@Autowired
	BatchErrorsRepository batchErrorsRepository;
	
	@Value("${input.file.path}")
	private String inputFilePath;
	@Autowired
	private JobCompletionNotificationListener jobCompletionListener;
	
	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Bean
	public Job employeeJob() {
		return new JobBuilder("employeeJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.listener(jobCompletionListener)
				.start(employeeStep()).build();
	}
	
	
	@Bean
	public Step employeeStep() {
		return new StepBuilder("employeeStep", jobRepository)
				.<EmployeeRequest, Employee>chunk(100, transactionManager)
				.reader(csvReader())
				.processor(processor())
				.writer(writer())
				.allowStartIfComplete(true)
				.build();
	}
	
	@Bean
	public FlatFileItemReader<EmployeeRequest> csvReader() {
		
		return new FlatFileItemReaderBuilder<EmployeeRequest>().name("employeeItemReader")
				.resource(new ClassPathResource("employeeData.csv"))
				.delimited()
				.delimiter(",")
				.names("employeeId", "firstName", "lastName", "email", "phoneNumber", "doj", "salary")
				.fieldSetMapper(new EmployeeMapper())
				.linesToSkip(1)
				.build();
	}
	
	@Bean
	public ItemProcessor<EmployeeRequest, Employee> processor() {
		return employeeRequest -> {
			Optional<Employee> existingEmployee = employeeRepository.findById(employeeRequest.getEmployeeId());
			Employee employee = existingEmployee.orElse(new Employee());
			employee.setFirstName(employeeRequest.getFirstName());
			employee.setLastName(employeeRequest.getLastName());
			employee.setEmail(employeeRequest.getEmail());
			employee.setDoj(employeeRequest.getDoj());
			employee.setSalary(BigDecimal.valueOf(employeeRequest.getSalary()));
			employee=	employeeRepository.save(employee);
			List<EmployeePhoneNumber> phoneNumbers = new LinkedList<>();
			//Retrieve existing phone number of an employee
			List<EmployeePhoneNumber> existingPhoneNumbers =  employeePhoneNumberRepository.findByEmployee(employee);
			
			// Create a map for existing phone numbers to facilitate quick lookups
			Map<String, EmployeePhoneNumber> phoneNumberMap = existingPhoneNumbers.stream()
			        .collect(Collectors.toMap(EmployeePhoneNumber::getPhoneNumber, phoneNumber -> phoneNumber));

			// List to keep track of phone numbers to be deleted
			List<EmployeePhoneNumber> toDelete = new LinkedList<>();

			// Iterate over existing phone numbers and mark for deletion if not present in the new list
			for (EmployeePhoneNumber existingPhone : existingPhoneNumbers) {
			    if (!employeeRequest.getPhoneNumber().contains(existingPhone.getPhoneNumber())) {
			        toDelete.add(existingPhone);
			    }
			}

			// Remove phone numbers that are no longer needed
			employeePhoneNumberRepository.deleteAll(toDelete);
			
			// Add new phone numbers
			for (String phone : employeeRequest.getPhoneNumber()) {
			    if (!phoneNumberMap.containsKey(phone)) {
			        EmployeePhoneNumber empPhone = new EmployeePhoneNumber();
			        empPhone.setPhoneNumber(phone);
			        empPhone.setEmployee(employee);

			        phoneNumbers.add(empPhone);
			    }
			}
			employee.setPhoneNumbers(phoneNumbers);
			return employee;
		};
	}
	
	@Bean
	public ItemWriter<Employee> writer() {
		return employees -> {
			for (Employee employee : employees) {
				try {
					employeeRepository.save(employee);
				} catch (Exception e) {
					BatchErrors batchErrorLog = new BatchErrors();
					batchErrorLog.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
					batchErrorLog.setFilename(inputFilePath);
					batchErrorLog.setMessage("Error processing Employee ID: " + employee.getEmployeeId());
					batchErrorLog.setError(e.getMessage());
					batchErrorsRepository.save(batchErrorLog);
				}
			}
		};
	}
}
