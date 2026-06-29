package com.smartcampus.enrollmentservice.service;

import com.smartcampus.enrollmentservice.model.Course;
import com.smartcampus.enrollmentservice.model.Enrollment;
import com.smartcampus.enrollmentservice.repository.CourseRepository;
import com.smartcampus.enrollmentservice.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class EnrollmentService {

    @Autowired
    private final CourseRepository courseRepository;
    @Autowired
    private final EnrollmentRepository enrollmentRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private final RestTemplate restTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private final String STUDENT_SERVICE_URL = "http://localhost:8080/students/";
    
    // In-memory locks allocated dynamically per courseId to protect seat counts
    private final ConcurrentHashMap<String, ReentrantLock> courseLocks = new ConcurrentHashMap<>();
    private final ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(20);

    public EnrollmentService(CourseRepository courseRepository, 
                             EnrollmentRepository enrollmentRepository, 
                             RestTemplate restTemplate) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public Course registerCourse(Course course) {
        if (course.getCourseId() == null || course.getCourseId().isEmpty()) {
            throw new IllegalArgumentException("Course ID is required");
        }
        
        course.setAvailableSeats(course.getMaxCapacity());
        Course saved = courseRepository.save(course);
        
        courseRepository.flush();
        entityManager.flush();
        
        return saved;
    }

    public List<Course> getCourseCatalog() {
        return courseRepository.findAll();
    }

    public CompletableFuture<Enrollment> submitConcurrentEnrollment(String studentId, String courseId) {
        return CompletableFuture.supplyAsync(() -> {
            
            ReentrantLock lock = courseLocks.computeIfAbsent(courseId, k -> new ReentrantLock());
            lock.lock();
            
            try {
                return transactionTemplate.execute(status -> {
                    Course course = courseRepository.findById(courseId)
                            .orElseThrow(() -> new IllegalArgumentException("Course not found"));

                    Optional<Enrollment> existing = enrollmentRepository.findByStudentIdAndCourseCourseId(studentId, courseId);
                    if (existing.isPresent() && !existing.get().getStatus().equals("DROPPED")) {
                        throw new IllegalStateException("Student is already registered or waitlisted for this course.");
                    }

                    Enrollment enrollment = new Enrollment();
                    enrollment.setStudentId(studentId);
                    enrollment.setCourse(course);

                    if (course.getAvailableSeats() > 0) {
                        course.setAvailableSeats(course.getAvailableSeats() - 1);
                        courseRepository.save(course); 
                        enrollment.setStatus("ENROLLED"); 
                    } else {
                        enrollment.setStatus("WAITLIST");
                    }

                    return enrollmentRepository.save(enrollment);
                });
                
            } finally {
                lock.unlock();
            }
        }, executorService);
    }

    public CompletableFuture<Enrollment> enrollWaitlist(String studentId, String courseId) {
        return CompletableFuture.supplyAsync(() -> {
            
            ReentrantLock lock = courseLocks.computeIfAbsent(courseId, k -> new ReentrantLock());
            lock.lock();
            
            try {
                return transactionTemplate.execute(status -> {
                    Course course = courseRepository.findById(courseId)
                            .orElseThrow(() -> new IllegalArgumentException("Course not found"));

                    Optional<Enrollment> existing = enrollmentRepository.findByStudentIdAndCourseCourseId(studentId, courseId);
                    
                    Enrollment enrollment;
                    if (existing.isPresent()) {
                        enrollment = existing.get();
                        if ("APPROVED".equals(enrollment.getStatus()) || "WAITLIST".equals(enrollment.getStatus())) {
                            throw new IllegalStateException("Student is already registered or waitlisted for this course.");
                        }
                    } else {
                        enrollment = new Enrollment();
                        enrollment.setStudentId(studentId);
                        enrollment.setCourse(course);
                    }

                    enrollment.setStatus("WAITLIST");

                    return enrollmentRepository.save(enrollment);
                });
            } finally {
                lock.unlock();
            }
        }, executorService);
    }

    public Enrollment enrollInCourseWithRetryAndTimeout(String studentId, String courseId) throws Exception {
        int maxAttempts = 3;
        int attempt = 0;
        long timeoutDurationSeconds = 5; 

        try {
            var studentProfile = restTemplate.getForObject(STUDENT_SERVICE_URL + studentId, java.util.Map.class);
            if (studentProfile == null || !"ACTIVE".equals(studentProfile.get("status"))) {
                throw new IllegalStateException("Student is not active or does not exist.");
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Communication with Student Service failed or profile invalid.");
        }

        ReentrantLock lock = courseLocks.computeIfAbsent(courseId, k -> new ReentrantLock());

        while (attempt < maxAttempts) {
            attempt++;

            if (lock.tryLock(timeoutDurationSeconds, TimeUnit.SECONDS)) {
                try {
                    Course course = courseRepository.findById(courseId)
                            .orElseThrow(() -> new IllegalArgumentException("Course does not exist."));

                    Optional<Enrollment> existing = enrollmentRepository.findByStudentIdAndCourseCourseId(studentId, courseId);
                    if (existing.isPresent() && "ENROLLED".equals(existing.get().getStatus())) {
                        throw new IllegalArgumentException("Student already has an active enrollment record for this course.");
                    }

                    if (course.getAvailableSeats() <= 0) {
                        throw new IllegalStateException("Course capacity check failed. Completely full.");
                    }

                    course.setAvailableSeats(course.getAvailableSeats() - 1);
                    courseRepository.save(course);

                    Enrollment enrollment = existing.orElse(new Enrollment());
                    enrollment.setStudentId(studentId);
                    enrollment.setCourse(course);
                    enrollment.setStatus("ENROLLED");

                    return enrollmentRepository.save(enrollment);

                } finally {
                    lock.unlock(); 
                }
            } else {
                System.out.println("Thread failed to acquire lock for course " + courseId + ". Retrying attempt #" + attempt);
                if (attempt < maxAttempts) {
                    Thread.sleep(300); 
                }
            }
        }

        throw new TimeoutException("Server was too busy processing concurrent enrollment requests for this course. Please try again later.");
    }
    
    @Transactional
    public Enrollment dropEnrollment(String studentId, String courseId) {
        Enrollment record = enrollmentRepository.findByStudentIdAndCourseCourseId(studentId, courseId)
            .orElseThrow(() -> new IllegalArgumentException("Enrollment record does not exist."));

        if (!"ENROLLED".equals(record.getStatus()) && !"APPROVED".equals(record.getStatus())) {
            throw new IllegalArgumentException("Enrollment cannot be dropped (Current status: " + record.getStatus() + ").");
        }

        record.setStatus("DROPPED");
        Enrollment savedRecord = enrollmentRepository.save(record);

        ReentrantLock lock = courseLocks.computeIfAbsent(courseId, k -> new ReentrantLock());
        lock.lock();
        try {
            Course course = record.getCourse();
            if (course.getAvailableSeats() < course.getMaxCapacity()) {
                course.setAvailableSeats(course.getAvailableSeats() + 1);
                courseRepository.save(course);
            }
        } finally {
            lock.unlock();
        }

        return savedRecord;
    }
    
    public List<Enrollment> getStudentSchedule(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }
    
    @Transactional
    public Enrollment changeEnrollmentStatus(String studentId, String courseId, String newStatus) {
        Enrollment record = enrollmentRepository.findByStudentIdAndCourseCourseId(studentId, courseId)
            .orElseThrow(() -> new IllegalArgumentException("Enrollment record does not exist."));

        // If they are dropping/withdrawing, give the seat back to the course
        if ("WITHDRAWN".equals(newStatus) || "DEFERRED".equals(newStatus)) {
            // Only return a seat if they were actually taking up a seat (APPROVED / ENROLLED)
            if ("APPROVED".equals(record.getStatus()) || "ENROLLED".equals(record.getStatus())) {
                ReentrantLock lock = courseLocks.computeIfAbsent(courseId, k -> new ReentrantLock());
                lock.lock();
                try {
                    Course course = record.getCourse();
                    if (course.getAvailableSeats() < course.getMaxCapacity()) {
                        course.setAvailableSeats(course.getAvailableSeats() + 1);
                        courseRepository.save(course);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        record.setStatus(newStatus);
        return enrollmentRepository.save(record);
    }
    
    public List<Enrollment> getCourseWaitlist(String courseId) {
        return enrollmentRepository.findByCourseCourseIdAndStatus(courseId, "WAITLIST");
    }
    
    public void simulateLongProcessingLock(String courseId) {
        ReentrantLock lock = courseLocks.computeIfAbsent(courseId, k -> new ReentrantLock());

        lock.lock();
        try {
            System.out.println("Lock held by test thread...");
            Thread.sleep(5000); // hold lock for 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}
