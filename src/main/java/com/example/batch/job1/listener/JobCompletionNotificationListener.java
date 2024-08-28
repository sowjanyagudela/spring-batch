package com.example.batch.job1.listener;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.batch.entity.BatchProcess;
import com.example.batch.repository.BatchProcessRepository;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

	@Autowired
	private BatchProcessRepository batchProcessRepository;
	
	@Value("${input.file.path}")
	private String inputFilePath;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		BatchProcess batchProcess = new BatchProcess();
		batchProcess.setId(jobExecution.getJobId());
		batchProcess.setProcessName("Spring Batch Process");
		batchProcess.setStartTime(new Timestamp(System.currentTimeMillis()));
		batchProcess.setProcessedFileName(inputFilePath);
		batchProcessRepository.save(batchProcess);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		Optional<BatchProcess> optionalBatchProcess = batchProcessRepository.findById(jobExecution.getJobId());
		if (optionalBatchProcess.isPresent()) {
			BatchProcess batchProcess = optionalBatchProcess.get();
			batchProcess.setEndTime(new Timestamp(System.currentTimeMillis()));

			// Calculate counts
			long insertedCount = jobExecution.getStepExecutions().stream().mapToLong(StepExecution::getWriteCount)
					.sum();

			int errorCount = jobExecution.getAllFailureExceptions().size();

			// Update batch process record
			batchProcess.setInsertedCount((int) insertedCount);
			batchProcess.setErrorCount(errorCount);
			batchProcess.setProcessedFileName(renameProcessedFile());
			batchProcessRepository.save(batchProcess);
		}
	}

	private String renameProcessedFile() {
		File oldFile = new File(inputFilePath);

		File parentDir = oldFile.getParentFile();
		if (!parentDir.canWrite()) {
			throw new IllegalStateException("Cannot write to directory: " + parentDir.getAbsolutePath());
		}

		// Construct the new file name with absolute paths
		String newFileName = "processed-employees-" + new SimpleDateFormat("MM-dd-yy-HH-mm-ss").format(new Date())
				+ ".csv";
		File newFile = new File(parentDir, newFileName);

		// Attempt to rename the file
		if (oldFile.renameTo(newFile)) {
			return newFile.getAbsolutePath(); // Return the absolute path of the renamed file
		} else {
			throw new IllegalStateException("Failed to rename file: " + oldFile.getAbsolutePath());
		}

	}
}
