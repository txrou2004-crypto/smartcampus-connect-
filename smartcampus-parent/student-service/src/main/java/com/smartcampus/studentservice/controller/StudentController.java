package com.smartcampus.studentservice.controller;

import com.smartcampus.studentservice.model.Student;
import com.smartcampus.studentservice.model.AcademicRecord;
import com.smartcampus.studentservice.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> getAll() {
        return service.getAllStudents();
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getById(@PathVariable String studentId) {
        Student student = service.getStudentById(studentId);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody Student student) {
        Student created = service.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{studentId}/records")
    public ResponseEntity<Student> addRecord(@PathVariable String studentId, @Valid @RequestBody AcademicRecord record) {
        Student updatedStudent = service.addAcademicRecordToStudent(studentId, record);
        return updatedStudent != null ? ResponseEntity.status(HttpStatus.CREATED).body(updatedStudent) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{studentId}/records/{recordId}")
    public ResponseEntity<AcademicRecord> getRecord(@PathVariable String studentId, @PathVariable Integer recordId) {
        AcademicRecord record = service.getAcademicRecord(studentId, recordId);
        return record != null ? ResponseEntity.ok(record) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{studentId}/records/{recordId}")
    public ResponseEntity<AcademicRecord> updateRecord(@PathVariable String studentId, @PathVariable Integer recordId,
                                                         @Valid @RequestBody AcademicRecord record) {
        AcademicRecord updated = service.updateAcademicRecord(studentId, recordId, record);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{studentId}/records/{recordId}")
    public ResponseEntity<Void> deleteRecord(@PathVariable String studentId, @PathVariable Integer recordId) {
        boolean deleted = service.deleteAcademicRecord(studentId, recordId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> update(@PathVariable String studentId, @Valid @RequestBody Student student) {
        Student updated = service.updateStudent(studentId, student);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> delete(@PathVariable String studentId) {
        boolean deleted = service.deleteStudent(studentId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
