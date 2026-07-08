package com.smartcampus.enrollmentservice.repository;

import com.smartcampus.enrollmentservice.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByStudentIdAndCourseCourseId(String studentId, String courseId);

    List<Enrollment> findByStudentId(String studentId);

    @Query("SELECT e FROM Enrollment e WHERE e.course.courseId = :courseId " +
           "AND e.status = 'WAITLISTED' ORDER BY e.createdAt ASC")
    List<Enrollment> findWaitlistedStudents(@Param("courseId") String courseId);

    Optional<Enrollment> findFirstByCourseCourseIdAndStatusOrderByCreatedAtAsc(String courseId, String status);
    
    List<Enrollment> findByCourseCourseIdAndStatus(String courseId, String status);
}
