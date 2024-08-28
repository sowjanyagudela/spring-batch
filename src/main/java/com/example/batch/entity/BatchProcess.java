package com.example.batch.entity;



import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "batch_process", schema = "img_batch")
@Getter
@Setter
public class BatchProcess {
	@Id
	private Long id;
	private String processName;
	private Date startTime;
	private Date endTime;
	private String processedFileName;
	private int insertedCount;
	private int updatedCount;
	private int errorCount;
}
