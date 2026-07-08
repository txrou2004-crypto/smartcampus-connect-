package com.smartcampus.reportinganalytics.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcampus.reportinganalytics.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/academic/scarcity")
    public ResponseEntity<List<Map<String, Object>>> getSeatScarcity() {
        return ResponseEntity.ok(analyticsService.getCourseSeatScarcityIndex());
    }

    @GetMapping("/academic/progress/{studentId}")
    public ResponseEntity<Map<String, Object>> getGraduationProgress(@PathVariable String studentId) {
        return ResponseEntity.ok(analyticsService.getGraduationProgress(studentId));
    }

    @GetMapping("/library/trends")
    public ResponseEntity<Map<String, Object>> getLibraryTrends() {
        return ResponseEntity.ok(analyticsService.getLibraryFlowTrends());
    }

    @GetMapping("/communication/ratio/{studentId}")
    public ResponseEntity<Map<String, Object>> getCommunicationRatio(@PathVariable String studentId) {
        return ResponseEntity.ok(analyticsService.getCommunicationChannelRatio(studentId));
    }
}
