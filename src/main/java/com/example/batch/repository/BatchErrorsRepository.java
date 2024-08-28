package com.example.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.batch.entity.BatchErrors;

public interface BatchErrorsRepository extends JpaRepository<BatchErrors, Integer> {

}
