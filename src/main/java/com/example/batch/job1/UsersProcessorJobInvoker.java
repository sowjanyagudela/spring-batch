package com.example.batch.job1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
public class UsersProcessorJobInvoker {

	private static final Logger logger = LoggerFactory.getLogger(UsersProcessorJobInvoker.class);

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Scheduled(cron = "${user.job.config.cron.schedule}")
	public void startProcessingUnverifiedAndNeverLoggedInUsersJob() {
		try {
			logger.info("In jobInvoker !!");
			getJobLauncher().run(job, new JobParametersBuilder().toJobParameters());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException ex) {
			logger.error("Failed to start Job name {} due to exception {}", job.getName(), ex.getMessage());
			ex.printStackTrace();
		}
	}
}
