package com.smartcampus.studentservice.service;

import com.smartcampus.studentservice.model.Student;
import com.smartcampus.studentservice.model.AcademicRecord;
import com.smartcampus.studentservice.repository.StudentRepository;
import com.smartcampus.studentservice.repository.AcademicRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final AcademicRecordRepository academicRecordRepository;

    public StudentService(StudentRepository repository, AcademicRecordRepository academicRecordRepository) {
        this.repository = repository;
        this.academicRecordRepository = academicRecordRepository;
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student getStudentById(String studentId) {
        return repository.findById(studentId).orElse(null);
    }

    @Transactional
    public Student createStudent(Student student) {
        if (student.getAcademicRecords() != null) {
            student.getAcademicRecords().forEach(record -> record.setStudent(student));
        }
        return repository.save(student);
    }

    @Transactional
    public Student addAcademicRecordToStudent(String studentId, AcademicRecord record) {
        Student student = repository.findById(studentId).orElse(null);
        if (student == null) return null;

        student.addAcademicRecord(record);
        return repository.save(student);
    }

    @Transactional
    public Student updateStudent(String studentId, Student updated) {
        Student s = repository.findById(studentId).orElse(null);
        if (s == null) return null;

        s.setFirstName(updated.getFirstName());
        s.setLastName(updated.getLastName());
        s.setEmail(updated.getEmail());
        s.setTelNumber(updated.getTelNumber());
        s.setProgrammeCode(updated.getProgrammeCode());
        s.setStatus(updated.getStatus());

        return repository.save(s);
    }

    @Transactional
    public boolean deleteStudent(String studentId) {
        if (repository.existsById(studentId)) {
            repository.deleteById(studentId);
            return true;
        }
        return false;
    }

    public AcademicRecord getAcademicRecord(String studentId, Integer recordId) {
        AcademicRecord record = academicRecordRepository.findById(recordId).orElse(null);
        if (record == null || !record.getStudent().getStudentId().equals(studentId)) {
            return null;
        }
        return record;
    }

    @Transactional
    public AcademicRecord updateAcademicRecord(String studentId, Integer recordId, AcademicRecord updated) {
        AcademicRecord record = getAcademicRecord(studentId, recordId);
        if (record == null) return null;

        record.setAcademicYear(updated.getAcademicYear());
        record.setSemester(updated.getSemester());
        record.setGpa(updated.getGpa());
        record.setCgpa(updated.getCgpa());
        record.setCreditsEarned(updated.getCreditsEarned());

        return academicRecordRepository.save(record);
    }

    @Transactional
    public boolean deleteAcademicRecord(String studentId, Integer recordId) {
        AcademicRecord record = getAcademicRecord(studentId, recordId);
        if (record == null) return false;

        academicRecordRepository.delete(record);
        return true;
    }
}
