package com.smartcampus.studentservice.repository;

import com.smartcampus.studentservice.model.AcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Integer> {
}
