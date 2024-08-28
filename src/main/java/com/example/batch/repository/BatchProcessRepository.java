package com.example.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.batch.entity.BatchProcess;

public interface BatchProcessRepository extends JpaRepository<BatchProcess, Long>{

}
